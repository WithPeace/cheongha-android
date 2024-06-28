package com.withpeace.withpeace.core.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class IsLoginUseCaseTest {
    private lateinit var isLoginUseCase: IsLoginUseCase
    private val tokenRepository: TokenRepository = mockk(relaxed = true)

    private fun initialize() = IsLoginUseCase(tokenRepository)

    @Test
    fun `로그인 상태이다`() = runTest {
        // given
        coEvery { tokenRepository.isLogin() } returns true
        isLoginUseCase = initialize()
        // when & then
        assertThat(isLoginUseCase()).isTrue()
    }

    @Test
    fun `비로그인 상태이다`() = runTest {
        // given
        coEvery { tokenRepository.isLogin() } returns false
        isLoginUseCase = initialize()
        // when & then
        assertThat(isLoginUseCase()).isFalse()
    }
}
