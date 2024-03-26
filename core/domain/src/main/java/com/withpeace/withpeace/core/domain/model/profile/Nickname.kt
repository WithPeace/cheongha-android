package com.withpeace.withpeace.core.domain.model.profile

/*
    Data와 UI에서 모두 쓰일 수 있는 도메인입니다.
 */
@JvmInline
value class Nickname private constructor(val value: String) {

    companion object {
        private const val NICKNAME_REGEX_PATTERN = "[a-zA-Z0-9가-힣]{2,10}"
        fun create(value: String): Nickname = Nickname(value)
        fun verifyNickname(nickname: String): Boolean {
            val regex = Regex(NICKNAME_REGEX_PATTERN)
            return regex.matches(nickname)
        }
    }
}
