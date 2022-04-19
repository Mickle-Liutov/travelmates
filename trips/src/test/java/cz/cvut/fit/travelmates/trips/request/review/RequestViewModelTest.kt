package cz.cvut.fit.travelmates.trips.request.review

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import cz.cvut.fit.travelmates.mainapi.trips.models.Request
import cz.cvut.fit.travelmates.mainapi.trips.models.RequestState
import cz.cvut.fit.travelmates.mainapi.user.PublicUser
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
class RequestViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var requestsRepository: RequestsRepository

    private lateinit var viewModel: RequestViewModel

    @MockK
    private lateinit var navigateBackObserver: Observer<Unit>

    @MockK
    private lateinit var acceptedObserver: Observer<Unit>

    @MockK
    private lateinit var acceptErrorObserver: Observer<Unit>

    @MockK
    private lateinit var navigateRejectObserver: Observer<Long>

    @MockK
    private lateinit var imageObserver: Observer<String?>

    @MockK
    private lateinit var nameObserver: Observer<String?>

    @MockK
    private lateinit var messageObserver: Observer<String?>

    @MockK
    private lateinit var contentObserver: Observer<Boolean>

    @MockK
    private lateinit var loadingObserver: Observer<Boolean>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)

        val savedStateHandle = SavedStateHandle(mapOf(KEY_REQUEST to TEST_REQUEST))
        viewModel = RequestViewModel(savedStateHandle, requestsRepository)

        viewModel.eventNavigateBack.observeForever(navigateBackObserver)
        viewModel.eventAccepted.observeForever(acceptedObserver)
        viewModel.eventAcceptError.observeForever(acceptErrorObserver)
        viewModel.eventNavigateReject.observeForever(navigateRejectObserver)
        viewModel.senderImage.observeForever(imageObserver)
        viewModel.senderName.observeForever(nameObserver)
        viewModel.message.observeForever(messageObserver)
        viewModel.contentVisible.observeForever(contentObserver)
        viewModel.loadingVisible.observeForever(loadingObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `pressing back triggers correct event`() {
        viewModel.onBackPressed()

        verify { navigateBackObserver.onChanged(null) }
    }

    @Test
    fun `pressing reject triggers correct event`() {
        viewModel.onRejectPressed()

        verify { navigateRejectObserver.onChanged(TEST_REQUEST.id) }
    }

    @Test
    fun `when requests repository fails, error event is called`() {
        coEvery { requestsRepository.acceptRequest(any()) } throws IllegalArgumentException()

        viewModel.onAcceptPressed()

        verify { acceptErrorObserver.onChanged(null) }
        verifyLoadingStateSequence()
    }

    @Test
    fun `when requests repository return success, correct events are triggered`() {
        viewModel.onAcceptPressed()

        verify(exactly = 1) { acceptedObserver.onChanged(null) }
        verify(exactly = 1) { navigateBackObserver.onChanged(null) }
        verifyLoadingStateSequence()
    }

    @Test
    fun `request details are setup correctly`() {
        verify { imageObserver.onChanged(TEST_REQUEST.user.picture) }
        verify { nameObserver.onChanged(TEST_REQUEST.user.name) }
        verify { messageObserver.onChanged(TEST_REQUEST.message) }
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
        private val TEST_REQUEST =
            Request(1, PublicUser("name", "picture"), emptyList(), "", "", RequestState.PENDING, "")
        private const val KEY_REQUEST = "request"
    }
}