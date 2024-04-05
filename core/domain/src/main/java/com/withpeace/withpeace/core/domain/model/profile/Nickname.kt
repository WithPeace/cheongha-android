package com.withpeace.withpeace.core.domain.model.profile

@JvmInline
value class Nickname(val value: String) {

    init {
        require(verifyFormat(value)) { "잘못된 닉네임 형식입니다" }
    }
    companion object {
        private const val NICKNAME_REGEX_PATTERN = "[a-zA-Z0-9가-힣]{2,10}"

        fun verifyFormat(nickname: String): Boolean {
            val regex = Regex(NICKNAME_REGEX_PATTERN)
            return regex.matches(nickname)
        }
    }
}