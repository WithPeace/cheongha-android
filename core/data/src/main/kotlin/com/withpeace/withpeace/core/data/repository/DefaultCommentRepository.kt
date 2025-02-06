package com.withpeace.withpeace.core.data.repository

import com.skydoves.sandwich.suspendOnSuccess
import com.withpeace.withpeace.core.data.util.handleApiFailure
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.model.post.ReportType
import com.withpeace.withpeace.core.domain.repository.CommentRepository
import com.withpeace.withpeace.core.network.di.request.CommonCommentRequest
import com.withpeace.withpeace.core.network.di.request.ReportTypeRequest
import com.withpeace.withpeace.core.network.di.service.CommentService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultCommentRepository @Inject constructor(
    private val commentService: CommentService,
    private val userRepository: DefaultUserRepository,
) : CommentRepository {
    override fun registerComment(
        targetType: String, targetId: Long, content: String,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Unit> =
        flow {
            commentService.registerComment(CommonCommentRequest(targetType, targetId, content))
                .suspendOnSuccess {
                    emit(Unit)
                }.handleApiFailure {
                    onErrorWithAuthExpired(it, onError)
                }
        }

    override fun reportComment(
        commentId: Long,
        reportType: ReportType,
        onError: suspend (CheonghaError) -> Unit,
    ): Flow<Unit> {
        return flow {
            commentService.reportComment(commentId, ReportTypeRequest(reportType.name))
                .suspendOnSuccess {
                    emit(Unit)
                }.handleApiFailure {
                    onErrorWithAuthExpired(it, onError)
                }
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
}