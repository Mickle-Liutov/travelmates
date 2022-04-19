package cz.cvut.fit.travelmates.auth.login

import com.amplifyframework.auth.AuthException
import cz.cvut.fit.travelmates.authapi.AuthRepository
import cz.cvut.fit.travelmates.mainapi.user.UserService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class LoginUseCaseTest {

    @MockK
    lateinit var authRepository: AuthRepository

    @MockK
    lateinit var userService: UserService

    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        loginUseCase = LoginUseCase(authRepository, userService)
    }

    @Test
    fun `successful login`(): Unit = runBlocking {
        coEvery { userService.loginUser() } returns Response.success(Unit)

        loginUseCase.invoke(TEST_EMAIL, TEST_PASSWORD)
    }

    @Test(expected = AuthException::class)
    fun `exception from auth repository should be propagated`(): Unit = runBlocking {
        val expectedException = AuthException("message", "recovery_suggestion")
        coEvery { authRepository.login(TEST_EMAIL, TEST_PASSWORD) } throws expectedException

        loginUseCase.invoke(TEST_EMAIL, TEST_PASSWORD)
    }

    @Test(expected = HttpException::class)
    fun `exception from userService logs user out of amplify`(): Unit = runBlocking {
        coEvery { userService.loginUser() } returns Response.error(401, "".toResponseBody(null))

        try {
            loginUseCase.invoke(TEST_EMAIL, TEST_PASSWORD)
        } catch (e: Exception) {
            coVerify(exactly = 1) { authRepository.logout() }
            throw e
        }
    }

    companion object {
        const val TEST_EMAIL = "email"
        const val TEST_PASSWORD = "password"
    }
}