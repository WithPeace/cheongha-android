package com.withpeace.withpeace.core.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.domain.model.AuthToken
import com.withpeace.withpeace.core.domain.repository.AuthTokenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SignUpUseCaseTest {
    private lateinit var signUpUseCase: SignUpUseCase
    private val authTokenRepository: AuthTokenRepository = mockk(relaxed = true)

    private fun initialize() = SignUpUseCase(authTokenRepository)

    @Test
    fun `회원가입에 성공하면, Token을 반환한다`() = runTest {
        // given
        coEvery {
            authTokenRepository.signUp(
                "Email",
                "nickname",
                onError = any(),
            )
        } returns flow { emit(AuthToken(accessToken = "test", refreshToken = "test")) }
        signUpUseCase = initialize()
        // when
        val actual = signUpUseCase("Email", "nickname", onError = {}).first()
        // then
        assertThat(actual).isEqualTo(
            AuthToken(
                accessToken = "test",
                refreshToken = "test",
            ),
        )
    }

    @Test
    fun `회원가입에 실패하면, 메세지가 담긴 실패응답을 반환한다`() = runTest {
        // given
        val errorMock = mockk<(String) -> Unit>(relaxed = true)
        coEvery {
            authTokenRepository.signUp(
                "Email",
                "nickname",
                onError = errorMock,
            )
        } returns flow { errorMock("test") }
        signUpUseCase = initialize()
        // when
        signUpUseCase("Email", "nickname", onError = errorMock).collect()
        // then
        coVerify { errorMock("test") }
    }
}
