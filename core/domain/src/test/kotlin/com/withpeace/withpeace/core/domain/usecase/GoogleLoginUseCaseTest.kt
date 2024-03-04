package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.AuthToken
import com.withpeace.withpeace.core.domain.repository.AuthTokenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GoogleLoginUseCaseTest {

    private lateinit var googleLoginUseCase: GoogleLoginUseCase
    private val authTokenRepository: AuthTokenRepository = mockk(relaxed = true)

    private fun initialize() = GoogleLoginUseCase(authTokenRepository)

    @Test
    fun `로그인에 실패하면, 실패 로직이 실행된다`() = runTest {
        // given
        val mockOnError = mockk<(String) -> Unit>(relaxed = true)
        coEvery {
            authTokenRepository.getTokenByGoogle(
                "test",
                onError = mockOnError,
            )
        } returns flow {
            mockOnError.invoke("test")
        }
        googleLoginUseCase = initialize()
        // when
        val mockOnSuccess = mockk<() -> Unit>(relaxed = true)
        googleLoginUseCase(
            idToken = "test",
            onError = mockOnError,
            onSuccess = mockOnSuccess,
        )
        // then
        coVerify { mockOnError.invoke("test") }
        coVerify(exactly = 0) { mockOnSuccess() }
    }

    @Test
    fun `로그인에 성공하면, 성공응답을 반환한다`() = runTest {
        // given
        coEvery {
            authTokenRepository.getTokenByGoogle(
                idToken = "test",
                onError = any(),
            )
        } returns flow {
            emit(AuthToken(accessToken = "test", refreshToken = "test"))
        }
        googleLoginUseCase = initialize()
        // when
        val onSuccess = mockk<() -> Unit>(relaxed = true)
        googleLoginUseCase(
            idToken = "test",
            onError = {},
            onSuccess = onSuccess,
        )
        // then
        coVerify { onSuccess() }
        coVerify { authTokenRepository.updateAccessToken("test") }
        coVerify { authTokenRepository.updateRefreshToken("test") }
    }
}
