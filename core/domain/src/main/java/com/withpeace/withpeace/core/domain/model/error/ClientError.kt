package com.withpeace.withpeace.core.domain.model.error

sealed interface ClientError : CheonghaError {
    sealed interface NicknameError : ClientError {
        data object Duplicated : NicknameError
        data object FormatInvalid : NicknameError
    }

    data object AuthExpired : ClientError
    data object ProfileNotChanged : ClientError

    sealed interface SearchError : ClientError {
        data object NoSearchResult : SearchError
        data object SingleCharacterSearch : SearchError
    }
}

class NoSearchResultException: IllegalStateException()
class SingleCharacterSearchException: IllegalStateException()

