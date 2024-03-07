package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.repository.TokenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GoogleLoginUseCaseTest {

    private lateinit var googleLoginUseCase: GoogleLoginUseCase
    private val tokenRepository: TokenRepository = mockk(relaxed = true)

    private fun initialize() = GoogleLoginUseCase(tokenRepository)

    @Test
    fun `로그인에 실패하면, 실패 로직이 실행된다`() = runTest {
        // given
        val onFailure = mockk<(String) -> Unit>(relaxed = true)
        coEvery {
            tokenRepository.getTokenByGoogle(
                "test",
                onError = onFailure,
            )
        } returns flow {
            onFailure.invoke("test")
        }
        googleLoginUseCase = initialize()
        // when
        googleLoginUseCase(
            idToken = "test",
            onError = onFailure,
        ).collect()
        // then
        coVerify { onFailure("test") }
    }

    @Test
    fun `로그인에 성공하면, 성공응답을 반환한다`() = runTest {
        // given
        val onSuccess = mockk<() -> Unit>(relaxed = true)
        coEvery {
            tokenRepository.getTokenByGoogle(
                idToken = "test",
                onError = any(),
            )
        } returns flow {
            onSuccess.invoke()
        }
        googleLoginUseCase = initialize()
        // when
        googleLoginUseCase(
            idToken = "test",
            onError = {},
        ).collect()
        // then
        coVerify { onSuccess() }
    }
}
