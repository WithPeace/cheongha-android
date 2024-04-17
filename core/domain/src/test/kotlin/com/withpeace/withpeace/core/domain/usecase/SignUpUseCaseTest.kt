package com.withpeace.withpeace.core.domain.usecase

import com.withpeace.withpeace.core.domain.model.SignUpInfo
import com.withpeace.withpeace.core.domain.model.error.CheonghaError
import com.withpeace.withpeace.core.domain.model.error.ClientError
import com.withpeace.withpeace.core.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SignUpUseCaseTest {
    private lateinit var signUpUseCase: SignUpUseCase
    private val userRepository: UserRepository = mockk(relaxed = true)

    private fun initialize() = SignUpUseCase(userRepository)

    @Test
    fun `회원가입에 성공하면, 성공응답을 반환한다`() = runTest {
        // given
        val onSuccess = mockk<() -> Unit>(relaxed = true)
        coEvery {
            userRepository.signUp(
                SignUpInfo(
                    "Email",
                    "nickname",
                ),
                onError = any(),
            )
        } returns flow { onSuccess.invoke() }
        signUpUseCase = initialize()
        // when
        signUpUseCase(SignUpInfo("Email", "nickname"), {}).collect()
        // then
        coVerify { onSuccess.invoke() }
    }

    @Test
    fun `회원가입에 실패하면, 메세지가 담긴 실패응답을 반환한다`() = runTest {
        // given
        val errorMock = mockk<(CheonghaError) -> Unit>(relaxed = true)
        coEvery {
            userRepository.signUp(
                SignUpInfo(
                    "Email",
                    "nickname",
                ),
                onError = errorMock,
            )
        } returns flow { errorMock(ClientError.AuthExpired) }
        signUpUseCase = initialize()
        // when
        signUpUseCase(
            SignUpInfo(
                "Email",
                "nickname",
            ),
            onError = errorMock,
        ).collect()
        // then
        coVerify { errorMock(ClientError.AuthExpired) }
    }
}
