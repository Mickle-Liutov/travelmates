package cz.cvut.fit.travelmates.trips.request.reject

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import cz.cvut.fit.travelmates.trips.request.RequestsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.mockk.verifySequence
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
class RejectDialogViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var requestsRepository: RequestsRepository

    private lateinit var viewModel: RejectDialogViewModel

    @MockK
    private lateinit var navigateBackObserver: Observer<Unit>

    @MockK
    private lateinit var rejectedObserver: Observer<Unit>

    @MockK
    private lateinit var popReviewObserver: Observer<Unit>

    @MockK
    private lateinit var contentObserver: Observer<Boolean>

    @MockK
    private lateinit var loadingObserver: Observer<Boolean>

    @MockK
    private lateinit var errorObserver: Observer<Unit>


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)

        val savedStateHandle = SavedStateHandle(mapOf(KEY_REQUEST_ID to REQUEST_ID))
        viewModel = RejectDialogViewModel(requestsRepository, savedStateHandle)

        viewModel.eventNavigateBack.observeForever(navigateBackObserver)
        viewModel.eventRejected.observeForever(rejectedObserver)
        viewModel.eventPopReview.observeForever(popReviewObserver)
        viewModel.contentVisible.observeForever(contentObserver)
        viewModel.loadingVisible.observeForever(loadingObserver)
        viewModel.eventError.observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `pressing cancel triggers correct event`() {
        viewModel.onCancelPressed()

        verify { navigateBackObserver.onChanged(null) }
    }

    @Test
    fun `when requests repository fails, error event is called`() {
        coEvery {
            requestsRepository.rejectRequest(
                any(),
                any(),
                any()
            )
        } throws IllegalArgumentException()

        viewModel.onRejectPressed()

        verify { errorObserver.onChanged(null) }
        verifyLoadingStateSequence()
    }

    @Test
    fun `when requests repository return success, correct events are triggered`() {
        viewModel.onRejectPressed()

        verify(exactly = 1) { rejectedObserver.onChanged(null) }
        verify(exactly = 1) { popReviewObserver.onChanged(null) }
        verify(exactly = 0) { errorObserver.onChanged(null) }
        verifyLoadingStateSequence()
    }

    private fun verifyLoadingStateSequence() {
        verifySequence {
            contentObserver.onChanged(true)
            contentObserver.onChanged(false)
            contentObserver.onChanged(true)
        }
        verifySequence {
            loadingObserver.onChanged(false)
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }
    }

    companion object {
        private const val REQUEST_ID = 2L
        private const val KEY_REQUEST_ID = "requestId"
    }
}