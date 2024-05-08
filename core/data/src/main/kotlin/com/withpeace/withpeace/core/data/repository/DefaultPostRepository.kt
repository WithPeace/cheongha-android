package com.withpeace.withpeace.core.data.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.skydoves.sandwich.suspendMapSuccess
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.data.paging.PostPagingSource
import com.withpeace.withpeace.core.data.util.convertToFile
import com.withpeace.withpeace.core.data.util.handleApiFailure
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostDetail
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.model.post.RegisterPost
import com.withpeace.withpeace.core.domain.model.post.ReportType
import com.withpeace.withpeace.core.domain.repository.PostRepository
import com.withpeace.withpeace.core.domain.repository.UserRepository
import com.withpeace.withpeace.core.network.di.request.CommentRequest
import com.withpeace.withpeace.core.network.di.request.ReportTypeRequest
import com.withpeace.withpeace.core.network.di.service.PostService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class DefaultPostRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val postService: PostService,
    private val userRepository: UserRepository,
) : PostRepository {
    override fun getPosts(
        postTopic: PostTopic,
        pageSize: Int,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<PagingData<Post>> = Pager(
        config = PagingConfig(pageSize),
        pagingSourceFactory = {
            PostPagingSource(
                postService = postService,
                postTopic = postTopic,
                pageSize = pageSize,
                onError = onError,
                userRepository = userRepository,
            )
        },
    ).flow

    override fun registerPost(
        post: RegisterPost,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Long> =
        flow {
            val imageRequestBodies = getImageRequestBodies(post.images.urls)
            val postRequestBodies = getPostRequestBodies(post)
            if (post.id == null) {
                postService.registerPost(postRequestBodies, imageRequestBodies)
                    .suspendMapSuccess {
                        emit(data.postId)
                    }.handleApiFailure {
                        onErrorWithAuthExpired(it, onError)
                    }
            } else {
                postService.editPost(post.id!!, postRequestBodies, imageRequestBodies)
                    .suspendMapSuccess {
                        emit(data.postId)
                    }.handleApiFailure {
                        onErrorWithAuthExpired(it, onError)
                    }
            }
        }.flowOn(Dispatchers.IO)

    override fun getPostDetail(
        postId: Long,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<PostDetail> = flow {
        postService.getPost(postId)
            .suspendMapSuccess {
                emit(data.toDomain())
            }.handleApiFailure {
                onErrorWithAuthExpired(it, onError)
            }
    }

    override fun deletePost(
        postId: Long,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Boolean> = flow {
        postService.deletePost(postId).suspendMapSuccess {
            emit(data)
        }.handleApiFailure {
            onErrorWithAuthExpired(it, onError)
        }
    }

    override fun registerComment(
        postId: Long,
        content: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Boolean> = flow {
        postService.registerComment(postId = postId, CommentRequest(content))
            .suspendMapSuccess {
                emit(data)
            }.handleApiFailure {
                onErrorWithAuthExpired(it, onError)
            }
    }

    override fun reportPost(
        postId: Long,
        reportType: ReportType,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Boolean> = flow {
        postService.reportPost(postId = postId, ReportTypeRequest(reportType.name))
            .suspendMapSuccess {
                emit(data)
            }.handleApiFailure {
                onErrorWithAuthExpired(it, onError)
            }
    }

    private fun getImageRequestBodies(
        imageUris: List<String>,
    ): List<MultipartBody.Part> {
        val imageFiles = imageUris.map { it.convertToFile(context) }
        return imageFiles.map { file ->
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(
                IMAGES_COLUMN,
                file.name,
                requestFile,
            )
        }
    }

    private fun getPostRequestBodies(post: RegisterPost): HashMap<String, RequestBody> {
        return HashMap<String, RequestBody>().apply {
            set(
                TYPE_COLUMN,
                post.topic.toString().toRequestBody("application/json".toMediaTypeOrNull()),
            )
            set(TITLE_COLUMN, post.title.toRequestBody("application/json".toMediaTypeOrNull()))
            set(CONTENT_COLUMN, post.content.toRequestBody("application/json".toMediaTypeOrNull()))
        }
    }

    private suspend fun onErrorWithAuthExpired(
        it: ResponseError,
        onError: suspend (CheonghaError) -> Unit,
    ) {
        if (it == ResponseError.INVALID_TOKEN_ERROR) {
            userRepository.logout(onError).collect {
                onError(ClientError.AuthExpired)
            }
        } else {
            onError(it)
        }
    }

    companion object {
        const val TITLE_COLUMN = "title"
        const val CONTENT_COLUMN = "content"
        const val TYPE_COLUMN = "type"
        const val IMAGES_COLUMN = "imageFiles"
    }
}
