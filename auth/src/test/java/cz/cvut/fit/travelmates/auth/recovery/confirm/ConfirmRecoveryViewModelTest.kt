package cz.cvut.fit.travelmates.auth.recovery.confirm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import cz.cvut.fit.travelmates.auth.shared.FormValidator
import cz.cvut.fit.travelmates.authapi.AuthRepository
import cz.cvut.fit.travelmates.core.resources.ResourcesProvider
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class ConfirmRecoveryViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val formValidator = FormValidator(0L)

    @MockK
    lateinit var resourcesProvider: ResourcesProvider

    @MockK
    lateinit var authRepository: AuthRepository

    private lateinit var viewModel: ConfirmRecoveryViewModel

    @MockK
    private lateinit var navigateBackObserver: Observer<Unit>

    @MockK
    private lateinit var errorObserver: Observer<String>

    @MockK
    private lateinit var navigateLoginObserver: Observer<Unit>

    @MockK
    private lateinit var showSuccessObserver: Observer<Unit>

    @MockK
    private lateinit var passwordMismatchObserver: Observer<Unit>

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
        viewModel = ConfirmRecoveryViewModel(formValidator, authRepository, resourcesProvider)
        every { resourcesProvider.getString(any()) } returns STRING_STUB

        viewModel.eventNavigateBack.observeForever(navigateBackObserver)
        viewModel.eventError.observeForever(errorObserver)
        viewModel.eventNavigateLogin.observeForever(navigateLoginObserver)
        viewModel.eventPasswordMismatch.observeForever(passwordMismatchObserver)
        viewModel.eventShowResetSuccess.observeForever(showSuccessObserver)
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
    fun `back button press triggers correct events`() {
        viewModel.onBackPressed()

        verify(exactly = 1) { navigateBackObserver.onChanged(null) }
    }

    @Test
    fun `when some field is empty, continue button is inactive`() {
        viewModel.typedPassword.value = EMPTY_FIELD
        viewModel.typedConfirmPassword.value = EMPTY_FIELD
        viewModel.typedSecurityCode.value = EMPTY_FIELD

        verify { isContinueActiveObserver.onChanged(false) }
    }

    @Test
    fun `when some field is invalid, continue button is inactive`() {
        viewModel.typedPassword.value = INVALID_PASSWORD
        viewModel.typedConfirmPassword.value = INVALID_PASSWORD
        viewModel.typedSecurityCode.value = SECURITY_CODE

        verify { isContinueActiveObserver.onChanged(false) }
    }

    @Test
    fun `when passwords don't match, correct event is called`() {
        viewModel.typedPassword.value = VALID_PASSWORD
        viewModel.typedConfirmPassword.value = OTHER_VALID_PASSWORD
        viewModel.typedSecurityCode.value = SECURITY_CODE

        viewModel.onContinuePressed()

        verify { passwordMismatchObserver.onChanged(null) }
    }

    @Test
    fun `when authRepository returns an error, an error event is triggered`() {
        viewModel.typedPassword.value = VALID_PASSWORD
        viewModel.typedConfirmPassword.value = VALID_PASSWORD
        viewModel.typedSecurityCode.value = SECURITY_CODE
        coEvery {
            authRepository.confirmPasswordRecovery(
                VALID_PASSWORD,
                SECURITY_CODE
            )
        } throws IllegalArgumentException()

        viewModel.onContinuePressed()

        verify(exactly = 0) { navigateLoginObserver.onChanged(null) }
        verify { errorObserver.onChanged(any()) }
        verifyLoadingStateSequence()
    }

    @Test
    fun `when recovery is successful, correct events are called`() {
        viewModel.typedPassword.value = VALID_PASSWORD
        viewModel.typedConfirmPassword.value = VALID_PASSWORD
        viewModel.typedSecurityCode.value = SECURITY_CODE

        viewModel.onContinuePressed()

        verify { navigateLoginObserver.onChanged(null) }
        verify { showSuccessObserver.onChanged(null) }
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
        private const val EMPTY_FIELD = "stub"
        private const val INVALID_PASSWORD = "1"
        private const val VALID_PASSWORD = "Password123"
        private const val OTHER_VALID_PASSWORD = "OtherPassword123"
        private const val SECURITY_CODE = "123456"
    }
}