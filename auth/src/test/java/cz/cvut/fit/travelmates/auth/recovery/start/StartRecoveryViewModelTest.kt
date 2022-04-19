package cz.cvut.fit.travelmates.auth.recovery.start

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import cz.cvut.fit.travelmates.authapi.AuthRepository
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
class StartRecoveryViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var authRepository: AuthRepository

    @MockK
    lateinit var resourcesProvider: ResourcesProvider

    private lateinit var viewModel: StartRecoveryViewModel

    @MockK
    private lateinit var navigateBackObserver: Observer<Unit>

    @MockK
    private lateinit var navigateConfirmObserver: Observer<Unit>

    @MockK
    private lateinit var navigateErrorObserver: Observer<String>

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
        viewModel = StartRecoveryViewModel(authRepository, resourcesProvider)
        every { resourcesProvider.getString(any()) } returns STRING_STUB

        viewModel.eventNavigateBack.observeForever(navigateBackObserver)
        viewModel.eventNavigateConfirm.observeForever(navigateConfirmObserver)
        viewModel.eventError.observeForever(navigateErrorObserver)
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
    fun `when email is empty, continue button is disabled`() {
        viewModel.typedEmail.value = EMPTY_FIELD

        verify { isContinueActiveObserver.onChanged(false) }
    }

    @Test
    fun `when email is valid, continue button is enabled`() {
        viewModel.typedEmail.value = VALID_EMAIl

        verify { isContinueActiveObserver.onChanged(true) }
    }

    @Test
    fun `when auth repository returns error, error event is called`() {
        viewModel.typedEmail.value = VALID_EMAIl
        coEvery { authRepository.startPasswordRecovery(VALID_EMAIl) } throws IllegalArgumentException()

        viewModel.onContinuePressed()

        verify { navigateErrorObserver.onChanged(any()) }
        verify(exactly = 0) { navigateConfirmObserver.onChanged(null) }
        verifyLoadingStateSequence()
    }

    @Test
    fun `when auth repository returns success, correct events are called`() {
        viewModel.typedEmail.value = VALID_EMAIl

        viewModel.onContinuePressed()
        verify { navigateConfirmObserver.onChanged(null) }
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
        private const val EMPTY_FIELD = ""
        private const val VALID_EMAIl = "mail@mail.com"
        private const val STRING_STUB = "stub"
    }
}