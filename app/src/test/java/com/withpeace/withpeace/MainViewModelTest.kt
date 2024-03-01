package com.withpeace.withpeace

import com.withpeace.withpeace.core.domain.usecase.IsLoginUseCase
import com.withpeace.withpeace.core.testing.MainDispatcherRule
import io.mockk.mockk
import org.junit.Rule

class MainViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var mainViewModel: MainViewModel
    private val isLoginUseCase: IsLoginUseCase = mockk()
    private fun initialize(): MainViewModel {
        return MainViewModel(isLoginUseCase)
    }
}
