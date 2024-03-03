package com.withpeace.withpeace.core.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.domain.model.Response
import com.withpeace.withpeace.core.domain.model.Token
import com.withpeace.withpeace.core.domain.repository.TokenRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SignUpUseCaseTest {
    private lateinit var signUpUseCase: SignUpUseCase
    private val tokenRepository: TokenRepository = mockk(relaxed = true)

    private fun initialize() = SignUpUseCase(tokenRepository)

    @Test
    fun `회원가입에 성공하면, Token을 반환한다`() = runTest {
        // given
        coEvery { tokenRepository.signUp("Email", "nickname") } returns flow {
            emit(Response.Success(Token(accessToken = "test", refreshToken = "test")))
        }
        signUpUseCase = initialize()
        // when
        val actual = signUpUseCase("Email", "nickname").first()
        // then
        assertThat(actual).isEqualTo(
            Response.Success(
                Token(
                    accessToken = "test",
                    refreshToken = "test",
                ),
            ),
        )
    }

    @Test
    fun `회원가입에 실패하면, 메세지가 담긴 실패응답을 반환한다`() = runTest {
        // given
        coEvery { tokenRepository.signUp("Email", "nickname") } returns flow {
            emit(Response.Failure(errorMessage = "실패"))
        }
        signUpUseCase = initialize()
        // when
        val actual = signUpUseCase("Email", "nickname").first()
        // then
        assertThat(actual).isEqualTo(Response.Failure<Token>(errorMessage = "실패"))
    }
}
