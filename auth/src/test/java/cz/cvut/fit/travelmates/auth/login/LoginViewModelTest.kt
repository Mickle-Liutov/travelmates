package cz.cvut.fit.travelmates.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import cz.cvut.fit.travelmates.core.resources.ResourcesProvider
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var login: LoginUseCase

    @MockK
    lateinit var resourcesProvider: ResourcesProvider

    private lateinit var viewModel: LoginViewModel

    @MockK
    private lateinit var navigateBackObserver: Observer<Unit>

    @MockK
    private lateinit var errorObserver: Observer<String>

    @MockK
    private lateinit var navigateMainObserver: Observer<Unit>

    @MockK
    private lateinit var navigateRecoveryObserver: Observer<Unit>

    @MockK
    private lateinit var isContinueActiveObserver: Observer<Boolean>

    @MockK
    private lateinit var contentVisibleObserver: Observer<Boolean>

    @MockK
    private lateinit var loadingVisibleObserver: Observer<Boolean>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = LoginViewModel(login, resourcesProvider)
        every { resourcesProvider.getString(any()) } returns STRING_STUB

        viewModel.eventNavigateBack.observeForever(navigateBackObserver)
        viewModel.eventError.observeForever(errorObserver)
        viewModel.eventNavigateMain.observeForever(navigateMainObserver)
        viewModel.eventNavigateRecovery.observeForever(navigateRecoveryObserver)
        viewModel.isContinueActive.observeForever(isContinueActiveObserver)
        viewModel.contentVisible.observeForever(contentVisibleObserver)
        viewModel.loadingVisible.observeForever(loadingVisibleObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `password recovery triggers correct event`() {
        viewModel.onPasswordRecoveryPressed()

        verify(exactly = 1) { navigateRecoveryObserver.onChanged(null) }
    }

    @Test
    fun `back press triggers correct event`() {
        viewModel.onBackPressed()

        verify(exactly = 1) { navigateBackObserver.onChanged(null) }
    }

    @Test
    fun `continue is not active when some fields are empty`() {
        viewModel.typedEmail.value = ""
        viewModel.typedPassword.value = ""

        verify { isContinueActiveObserver.onChanged(false) }
    }

    @Test
    fun `continue active when fields are filled`() {
        viewModel.typedEmail.value = TEST_EMAIL
        viewModel.typedPassword.value = TEST_PASSWORD

        verify { isContinueActiveObserver.onChanged(true) }
    }

    @Test
    fun `successful login navigates to main, has correct loading states`() {
        viewModel.typedEmail.value = TEST_EMAIL
        viewModel.typedPassword.value = TEST_PASSWORD

        viewModel.onLoginPressed()

        verify { navigateMainObserver.onChanged(null) }
        verifyLoadingStateSequence()
    }

    @Test
    fun `failed login doesn't navigate, displays an error, has correct loading states`() {
        viewModel.typedEmail.value = TEST_EMAIL
        viewModel.typedPassword.value = TEST_PASSWORD
        coEvery { login.invoke(TEST_EMAIL, TEST_PASSWORD) } throws IllegalArgumentException()

        viewModel.onLoginPressed()

        verify { errorObserver.onChanged(any()) }
        verify(exactly = 0) { navigateMainObserver.onChanged(null) }
        verifyLoadingStateSequence()
    }

    private fun verifyLoadingStateSequence() {
        verifySequence {
            contentVisibleObserver.onChanged(true)
            contentVisibleObserver.onChanged(false)
            contentVisibleObserver.onChanged(true)
        }
        verifySequence {
            loadingVisibleObserver.onChanged(false)
            loadingVisibleObserver.onChanged(true)
            loadingVisibleObserver.onChanged(false)
        }
    }

    companion object {
        private const val STRING_STUB = "stub"
        private const val TEST_EMAIL = "email"
        private const val TEST_PASSWORD = "password"
    }
}