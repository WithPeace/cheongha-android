package com.withpeace.withpeace.core.domain.usecase

import androidx.paging.PagingData
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.model.error.SingleCharacterSearchException
import com.withpeace.withpeace.core.domain.model.policy.YouthPolicy
import com.withpeace.withpeace.core.domain.model.search.SearchKeyword
import com.withpeace.withpeace.core.domain.repository.YouthPolicyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val youthPolicyRepository: YouthPolicyRepository,
) {
    operator fun invoke(
        onError: suspend (CheonghaError) -> Unit,
        keyword: String,
        onReceiveTotalCount: (Int) -> Unit,
    ): Flow<PagingData<Pair<Int, YouthPolicy>>> {
        if (SearchKeyword.validate(keyword).not()) {
            throw SingleCharacterSearchException()
        }
        return youthPolicyRepository.search(
            searchKeyword = SearchKeyword(keyword),
            onError = onError,
            onReceiveTotalCount
        )
    }
}