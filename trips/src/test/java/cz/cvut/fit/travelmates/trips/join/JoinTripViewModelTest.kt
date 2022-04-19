package cz.cvut.fit.travelmates.trips.join

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.trips.models.*
import cz.cvut.fit.travelmates.trips.TripsRepository
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
import java.time.LocalDate

@ExperimentalCoroutinesApi
class JoinTripViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var tripsRepository: TripsRepository

    private lateinit var viewModel: JoinTripViewModel

    @MockK
    private lateinit var equipmentObserver: Observer<List<ProvidedRequirement>>

    @MockK
    private lateinit var requestSentObserver: Observer<Unit>

    @MockK
    private lateinit var navigateBackObserver: Observer<Unit>

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

        val savedStateHandle = SavedStateHandle(mapOf(KEY_TRIP to TRIP))
        viewModel = JoinTripViewModel(savedStateHandle, tripsRepository)

        viewModel.equipment.observeForever(equipmentObserver)
        viewModel.eventRequestSent.observeForever(requestSentObserver)
        viewModel.contentVisible.observeForever(contentObserver)
        viewModel.loadingVisible.observeForever(loadingObserver)
        viewModel.eventNavigateBack.observeForever(navigateBackObserver)
        viewModel.eventError.observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `pressing back button triggers correct event`() {
        viewModel.onBackPressed()

        verify(exactly = 1) { navigateBackObserver.onChanged(null) }
    }

    @Test
    fun `after checking requirement, change is reflected is exposed requirements`() {
        val expected = TRIP_REQUIREMENTS.mapIndexed { index, requirement ->
            if (index == 0) {
                requirement.copy(isFulfilled = true)
            } else requirement
        }.map { ProvidedRequirement(it.id, it.name, it.isFulfilled) }

        viewModel.onItemChecked(TRIP_REQUIREMENTS.first().id, true)

        verify { equipmentObserver.onChanged(expected) }
    }

    @Test
    fun `when trips repository fails, correct events are triggered`() {
        TRIP_REQUIREMENTS.take(2).forEach {
            viewModel.onItemChecked(it.id, true)
        }
        viewModel.typedContact.value = TEST_CONTACT
        viewModel.typedMessage.value = TEST_MESSAGE
        val requestDto =
            NewJoinRequestDto(TEST_MESSAGE, TEST_CONTACT, TRIP_REQUIREMENTS.take(2).map { it.id })
        coEvery {
            tripsRepository.sendJoinRequest(
                TRIP.id,
                requestDto
            )
        } throws IllegalArgumentException()

        viewModel.onSendPressed()

        verify { errorObserver.onChanged(null) }
        verifyLoadingStateSequence()
    }

    @Test
    fun `when trips repository is successful, correct events are triggered`() {
        viewModel.typedContact.value = TEST_CONTACT
        viewModel.typedMessage.value = TEST_MESSAGE

        viewModel.onSendPressed()

        verify(exactly = 1) { requestSentObserver.onChanged(null) }
        verify(exactly = 1) { navigateBackObserver.onChanged(null) }
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
        private val TRIP_REQUIREMENTS = listOf(
            TripRequirement(0, false, "req1"),
            TripRequirement(1, false, "req2"),
            TripRequirement(2, false, "req3"),
        )
        private val TRIP = Trip(
            0,
            "title1",
            "",
            LocalDate.now(),
            TripState.GATHERING,
            Location(1.0, 2.0),
            TRIP_REQUIREMENTS,
            TripMember("", "", "", "", emptyList()),
            emptyList(),
            UserType.GUEST,
            emptyList(),
            null,
            emptyList()
        )
        private const val KEY_TRIP = "trip"
        private const val TEST_MESSAGE = "message"
        private const val TEST_CONTACT = "contact"
    }
}