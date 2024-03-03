package com.withpeace.withpeace.core.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.domain.model.Response
import com.withpeace.withpeace.core.domain.model.Token
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GoogleLoginUseCaseTest {

    private lateinit var googleLoginUseCase: GoogleLoginUseCase
    private val tokenRepository: TokenRepository = mockk(relaxed = true)

    private fun initialize() = GoogleLoginUseCase(tokenRepository)

    @Test
    fun `로그인에 실패하면, 실패응답을 반환한다`() = runTest {
        // given
        coEvery { tokenRepository.googleLogin("test") } returns flow {
            emit(Response.Failure())
        }
        googleLoginUseCase = initialize()
        // when
        val actual = googleLoginUseCase("test").first()
        // then
        assertThat(actual).isEqualTo(Response.Failure<Unit>())
    }

    @Test
    fun `로그인에 성공하면, 성공응답을 반환한다`() = runTest {
        // given
        coEvery { tokenRepository.googleLogin("test") } returns flow {
            emit(Response.Success(Token(accessToken = "test", refreshToken = "test")))
        }
        googleLoginUseCase = initialize()
        // when
        val actual = googleLoginUseCase("test").first()
        // then
        assertThat(actual).isEqualTo(Response.Success(Unit))
        coVerify { tokenRepository.updateAccessToken("test") }
        coVerify { tokenRepository.updateRefreshToken("test") }
    }
}
