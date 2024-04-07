package com.withpeace.withpeace.core.data.repository

import android.content.Context
import android.net.Uri
import com.skydoves.sandwich.messageOrNull
import com.skydoves.sandwich.suspendMapSuccess
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.withpeace.withpeace.core.data.mapper.toDomain
import com.withpeace.withpeace.core.data.util.convertToFile
import com.withpeace.withpeace.core.domain.model.WithPeaceError
import com.withpeace.withpeace.core.domain.model.WithPeaceError.GeneralError
import com.withpeace.withpeace.core.domain.model.WithPeaceError.UnAuthorized
import com.withpeace.withpeace.core.domain.model.post.Post
import com.withpeace.withpeace.core.domain.model.post.PostDetail
import com.withpeace.withpeace.core.domain.model.post.PostTopic
import com.withpeace.withpeace.core.domain.model.post.RegisterPost
import com.withpeace.withpeace.core.domain.repository.PostRepository
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
) : PostRepository {
    override fun getPosts(
        postTopic: PostTopic, pageIndex: Int,
        pageSize: Int,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<List<Post>> =
        flow {
            postService.getPosts(
                postTopic = postTopic.name,
                pageIndex = pageIndex, pageSize = pageSize,
            ).suspendMapSuccess {
                emit(data.map { it.toDomain() })
            }.suspendOnError {
                if (statusCode.code == 401) {
                    onError(
                        UnAuthorized(
                            statusCode.code,
                            message = null,
                        ),
                    )
                } else {
                    onError(GeneralError(statusCode.code, messageOrNull))
                }
            }.suspendOnException {
                onError(GeneralError(message = messageOrNull))
            }
        }.flowOn(Dispatchers.IO)

    override fun registerPost(
        post: RegisterPost,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<Long> =
        flow {
            val imageRequestBodies = getImageRequestBodies(post.images.urls)
            val postRequestBodies = getPostRequestBodies(post)
            if (post.id == null) {
                postService.registerPost(postRequestBodies, imageRequestBodies)
                    .suspendMapSuccess {
                        emit(data.postId)
                    }.suspendOnError {
                        if (statusCode.code == 401) {
                            onError(UnAuthorized())
                        } else {
                            onError(GeneralError(statusCode.code, messageOrNull))
                        }
                    }.suspendOnException {
                        onError(GeneralError(message = messageOrNull))
                    }
            } else {
                postService.editPost(post.id!!, postRequestBodies, imageRequestBodies)
                    .suspendMapSuccess {
                        emit(data.postId)
                    }.suspendOnError {
                        if (statusCode.code == 401) {
                            onError(UnAuthorized())
                        } else {
                            onError(GeneralError(statusCode.code, messageOrNull))
                        }
                    }.suspendOnException {
                        onError(GeneralError(message = messageOrNull))
                    }
            }
        }.flowOn(Dispatchers.IO)

    override fun getPostDetail(
        postId: Long,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<PostDetail> = flow {
        postService.getPost(postId)
            .suspendMapSuccess {
                emit(data.toDomain())
            }.suspendOnError {
                if (statusCode.code == 401) onError(UnAuthorized())
                else onError(GeneralError(statusCode.code, messageOrNull))
            }.suspendOnException {
                onError(GeneralError(message = messageOrNull))
            }
    }

    override fun deletePost(
        postId: Long,
        onError: suspend (WithPeaceError) -> Unit,
    ): Flow<Boolean> = flow {
        postService.deletePost(postId).suspendMapSuccess {
            emit(data)
        }.suspendOnError {
            if (statusCode.code == 401) onError(UnAuthorized())
            else onError(GeneralError(statusCode.code, messageOrNull))
        }.suspendOnException {
            onError(GeneralError(message = messageOrNull))
        }
    }

    private fun getImageRequestBodies(
        imageUris: List<String>,
    ): List<MultipartBody.Part> {
        val imageFiles = imageUris.map { Uri.parse(it).convertToFile(context) }
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

    companion object {
        const val TITLE_COLUMN = "title"
        const val CONTENT_COLUMN = "content"
        const val TYPE_COLUMN = "type"
        const val IMAGES_COLUMN = "images"
    }
}
