package com.withpeace.withpeace

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.withpeace.withpeace.core.domain.model.role.Role
import com.withpeace.withpeace.core.domain.usecase.CheckAppUpdateUseCase
import com.withpeace.withpeace.core.domain.usecase.IsLoginUseCase
import com.withpeace.withpeace.core.testing.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    // @get:Rule
    // val dispatcherRule = MainDispatcherRule()
    //
    // private lateinit var mainViewModel: MainViewModel
    // private val isLoginUseCase: IsLoginUseCase = mockk(relaxed = true)
    // private val checkAppUpdateUseCase: CheckAppUpdateUseCase = mockk(relaxed = true)
    // private fun initialize() {
    //     mainViewModel = MainViewModel(isLoginUseCase, checkAppUpdateUseCase)
    // }
    //
    // @Test
    // fun `현재 로그인을 안하고, 업데이트를 해야하면 업데이트 상태다 `() = runTest {
    //     // given
    //     coEvery {
    //         checkAppUpdateUseCase(
    //             0,
    //             onError = any(),
    //         )
    //     } returns flow {
    //         emit(true)
    //         coEvery { isLoginUseCase() } returns false
    //     }
    //
    //     initialize()
    //     // when && test
    //     mainViewModel.uiState.test {
    //         val actual = awaitItem()
    //         assertThat(actual).isEqualTo(MainUiState.Update)
    //     }
    // }
    //
    // @Test
    // fun `현재 로그인을 하고 업데이트 해야하면 업데이트 상태이다`() = runTest {
    //     // given
    //     coEvery { isLoginUseCase() } returns true
    //     coEvery {
    //         checkAppUpdateUseCase(
    //             0,
    //             onError = any(),
    //         )
    //     } returns flow {
    //         emit(true)
    //     }
    //
    //     // when && test
    //     mainViewModel.uiState.test {
    //         val actual = awaitItem()
    //         assertThat(actual).isEqualTo(MainUiState.Update)
    //     }
    // }
}
