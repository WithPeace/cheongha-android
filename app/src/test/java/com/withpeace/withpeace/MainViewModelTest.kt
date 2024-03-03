package com.withpeace.withpeace

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.domain.usecase.IsLoginUseCase
import com.withpeace.withpeace.core.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var mainViewModel: MainViewModel
    private val isLoginUseCase: IsLoginUseCase = mockk()
    private fun initialize(): MainViewModel {
        return MainViewModel(isLoginUseCase)
    }

    @Test
    fun `현재 로그인된 상태가 아니라면, 로그인 상태가 아니다`() = runTest {
        // given
        coEvery { isLoginUseCase() } returns false
        mainViewModel = initialize()

        // when && test
        mainViewModel.isLogin.test {
            val actual = awaitItem()
            assertThat(actual).isFalse()
        }
    }

    @Test
    fun `현재 로그인된 상태라면, 로그인 상태이다`() = runTest {
        // given
        coEvery { isLoginUseCase() } returns true
        mainViewModel = initialize()

        // when && test
        mainViewModel.isLogin.test {
            val actual = awaitItem()
            assertThat(actual).isTrue()
        }
    }
}
