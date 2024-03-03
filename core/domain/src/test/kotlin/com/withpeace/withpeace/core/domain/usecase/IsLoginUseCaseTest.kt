package com.withpeace.withpeace.core.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class IsLoginUseCaseTest {
    private lateinit var isLoginUseCase: IsLoginUseCase
    private val tokenRepository: TokenRepository = mockk(relaxed = true)

    private fun initialize() = IsLoginUseCase(tokenRepository)

    @Test
    fun `토큰이 있다면, 로그인 상태이다`() = runTest {
        // given
        coEvery { tokenRepository.getAccessToken() } returns flow {
            emit("test")
        }
        isLoginUseCase = initialize()
        // when & then
        assertThat(isLoginUseCase()).isTrue()
    }

    @Test
    fun `토큰이 없다면, 비로그인 상태이다`() = runTest {
        // given
        coEvery { tokenRepository.getAccessToken() } returns flow {
            emit(null)
        }
        isLoginUseCase = initialize()
        // when & then
        assertThat(isLoginUseCase()).isFalse()
    }
}
