package cz.cvut.fit.travelmates.auth.register

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
class RegisterViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val formValidator = FormValidator(0L)

    @MockK
    lateinit var resourcesProvider: ResourcesProvider

    @MockK
    lateinit var authRepository: AuthRepository

    private lateinit var viewModel: RegisterViewModel

    @MockK
    private lateinit var navigateBackObserver: Observer<Unit>

    @MockK
    private lateinit var errorObserver: Observer<String>

    @MockK
    private lateinit var eventRegisteredObserver: Observer<Unit>

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
        viewModel = RegisterViewModel(formValidator, authRepository, resourcesProvider)
        every { resourcesProvider.getString(any()) } returns STRING_STUB

        viewModel.eventNavigateBack.observeForever(navigateBackObserver)
        viewModel.eventError.observeForever(errorObserver)
        viewModel.eventRegistered.observeForever(eventRegisteredObserver)
        viewModel.eventPasswordMismatch.observeForever(passwordMismatchObserver)
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
        viewModel.typedEmail.value = EMPTY_FIELD
        viewModel.typedPassword.value = EMPTY_FIELD
        viewModel.typedConfirmPassword.value = EMPTY_FIELD
        viewModel.typedName.value = EMPTY_FIELD

        verify { isContinueActiveObserver.onChanged(false) }
    }

    @Test
    fun `when some field is invalid, continue button is inactive`() {
        viewModel.typedPassword.value = INVALID_PASSWORD
        viewModel.typedConfirmPassword.value = INVALID_PASSWORD
        viewModel.typedEmail.value = VALID_EMAIL
        viewModel.typedName.value = VALID_EMAIL

        verify { isContinueActiveObserver.onChanged(false) }
    }

    @Test
    fun `when passwords don't match, correct event is called`() {
        viewModel.typedPassword.value = VALID_PASSWORD
        viewModel.typedConfirmPassword.value = OTHER_VALID_PASSWORD
        viewModel.typedEmail.value = VALID_EMAIL
        viewModel.typedName.value = VALID_EMAIL

        viewModel.onRegisterPressed()

        verify { passwordMismatchObserver.onChanged(null) }
    }

    @Test
    fun `when authRepository returns an error, an error event is triggered`() {
        viewModel.typedPassword.value = VALID_PASSWORD
        viewModel.typedConfirmPassword.value = VALID_PASSWORD
        viewModel.typedEmail.value = VALID_EMAIL
        viewModel.typedName.value = VALID_EMAIL
        coEvery {
            authRepository.register(VALID_EMAIL, VALID_PASSWORD, VALID_EMAIL)
        } throws IllegalArgumentException()

        viewModel.onRegisterPressed()

        verify(exactly = 0) { eventRegisteredObserver.onChanged(null) }
        verify { errorObserver.onChanged(any()) }
        verifyLoadingStateSequence()
    }

    @Test
    fun `when recovery is successful, correct events are called`() {
        viewModel.typedPassword.value = VALID_PASSWORD
        viewModel.typedConfirmPassword.value = VALID_PASSWORD
        viewModel.typedEmail.value = VALID_EMAIL
        viewModel.typedName.value = VALID_EMAIL

        viewModel.onRegisterPressed()

        verify { eventRegisteredObserver.onChanged(null) }
        verify { navigateBackObserver.onChanged(null) }
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
        private const val VALID_EMAIL = "mail@mail.com"
    }
}