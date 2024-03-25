package com.withpeace.withpeace.core.domain.model.profile

data class ProfileInfo(
    val nickname: String,
    val profileImageUrl: String,
    val email: String
) {
    companion object {
        private const val NICKNAME_REGEX_PATTERN = "[가-힣a-zA-Z]{2,10}"

        fun validateNickname(nickname: String): Boolean {
            val regex = Regex(NICKNAME_REGEX_PATTERN)
            return regex.matches(nickname)
        }
    }
}
