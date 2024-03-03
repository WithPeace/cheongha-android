package com.withpeace.withpeace.feature.login

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.domain.model.Response
import com.withpeace.withpeace.core.domain.model.Token
import com.withpeace.withpeace.core.domain.usecase.GoogleLoginUseCase
import com.withpeace.withpeace.core.domain.usecase.SignUpUseCase
import com.withpeace.withpeace.core.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest() {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LoginViewModel
    private val googleLoginUseCase: GoogleLoginUseCase = mockk()
    private val signUpUseCase: SignUpUseCase = mockk(relaxed = true)

    private fun initialize(): LoginViewModel {
        return LoginViewModel(googleLoginUseCase, signUpUseCase)
    }

    @Test
    fun `구글 로그인에 성공하면 로그인 성공 이벤트를 발생한다`() = runTest {
        // given
        coEvery { googleLoginUseCase("test") } returns flow {
            emit(Response.Success(Unit))
        }
        viewModel = initialize()

        // when & then
        viewModel.loginUiEvent.test {
            viewModel.googleLogin("test")
            val actual = awaitItem()
            assertThat(actual).isEqualTo(LoginUiEvent.LoginSuccess)
        }
    }

    @Test
    fun `구글 로그인에 실패하면 로그인 실패 이벤트를 발생한다`() = runTest {
        // given
        coEvery { googleLoginUseCase("test") } returns flow {
            emit(Response.Failure())
        }
        viewModel = initialize()

        // when & then
        viewModel.loginUiEvent.test {
            viewModel.googleLogin("test")
            val actual = awaitItem()
            assertThat(actual).isEqualTo(LoginUiEvent.LoginFail)
        }
    }

    @Test
    fun `회원가입 성공하면 회원가입 성공 이벤트를 발생한다`() = runTest {
        // given
        coEvery { signUpUseCase(email = "abc", nickname = "abc") } returns flow {
            emit(
                Response.Success<Token>(
                    data = Token("testAccessToken", "testRefreshToken"),
                ),
            )
        }
        viewModel = initialize()

        // when & then
        viewModel.loginUiEvent.test {
            viewModel.signUp("abc","abc")
            val actual = awaitItem()
            assertThat(actual).isEqualTo(LoginUiEvent.SignUpSuccess)
        }
    }

    @Test
    fun `회원가입 실패하면 회원가입 실패 이벤트를 발생한다`() = runTest {
        // given
        coEvery { signUpUseCase("abc","abc") } returns flow {
            emit(Response.Failure(errorMessage = "error"))
        }
        viewModel = initialize()

        // when & then
        viewModel.loginUiEvent.test {
            viewModel.signUp("abc","abc")
            val actual = awaitItem()
            assertThat(actual).isEqualTo(LoginUiEvent.SignUpFail("error"))
        }
    }
}
