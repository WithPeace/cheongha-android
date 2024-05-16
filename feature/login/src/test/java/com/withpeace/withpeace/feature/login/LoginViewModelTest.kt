package com.withpeace.withpeace.feature.login

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ResponseError
import com.withpeace.withpeace.core.domain.model.role.Role
import com.withpeace.withpeace.core.domain.usecase.GoogleLoginUseCase
import com.withpeace.withpeace.core.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: LoginViewModel
    private val googleLoginUseCase: GoogleLoginUseCase = mockk()

    private fun initialize(): LoginViewModel {
        return LoginViewModel(googleLoginUseCase)
    }

    @Test
    fun `구글 로그인에 성공하고 권한이 유저이면 로그인 성공 이벤트를 발생한다`() = runTest {
        // given
        coEvery {
            googleLoginUseCase(
                "test",
                onError = any(),
            )
        } returns flow {
            emit(Role.USER)
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
    fun `구글 로그인에 성공하고 권한이 게스트이면 회원가입 필요 이벤트를 발생한다`() = runTest {
        // given
        coEvery {
            googleLoginUseCase(
                "test",
                onError = any(),
            )
        } returns flow {
            emit(Role.GUEST)
        }
        viewModel = initialize()

        // when & then
        viewModel.loginUiEvent.test {
            viewModel.googleLogin("test")
            val actual = awaitItem()
            assertThat(actual).isEqualTo(LoginUiEvent.SignUpNeeded)
        }
    }

    @Test
    fun `구글 로그인에 성공하고 권한이 알 수 없으면 로그인 실패 이벤트를 발생한다`() = runTest {
        // given
        coEvery {
            googleLoginUseCase(
                "test",
                onError = any(),
            )
        } returns flow {
            emit(Role.UNKNOWN)
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
    fun `구글 로그인에 실패하면 로그인 실패 이벤트를 발생한다`() = runTest {
        // given
        val onFailSlot = slot<suspend (CheonghaError) -> Unit>()
        coEvery {
            googleLoginUseCase(
                "test",
                onError = capture(onFailSlot),
            )
        } returns flow { onFailSlot.captured(ResponseError.UNKNOWN_ERROR) }

        viewModel = initialize()
        // when & then
        viewModel.loginUiEvent.test {
            viewModel.googleLogin("test")
            val actual = awaitItem()
            assertThat(actual).isEqualTo(LoginUiEvent.LoginFail)
        }
    }
}
