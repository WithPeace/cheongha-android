package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.repository.TokenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SignUpUseCaseTest {
    private lateinit var signUpUseCase: SignUpUseCase
    private val tokenRepository: TokenRepository = mockk(relaxed = true)

    private fun initialize() = SignUpUseCase(tokenRepository)

    @Test
    fun `회원가입에 성공하면, 성공응답을 반환한다`() = runTest {
        // given
        val onSuccess = mockk<() -> Unit>(relaxed = true)
        coEvery {
            tokenRepository.signUp(
                "Email",
                "nickname",
                onError = any(),
            )
        } returns flow { onSuccess.invoke() }
        signUpUseCase = initialize()
        // when
        signUpUseCase("Email", "nickname", {}).collect()
        // then
        coVerify { onSuccess.invoke() }
    }

    @Test
    fun `회원가입에 실패하면, 메세지가 담긴 실패응답을 반환한다`() = runTest {
        // given
        val errorMock = mockk<(String) -> Unit>(relaxed = true)
        coEvery {
            tokenRepository.signUp(
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
