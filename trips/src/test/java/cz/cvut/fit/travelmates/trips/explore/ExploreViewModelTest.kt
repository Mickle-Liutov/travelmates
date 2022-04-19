package cz.cvut.fit.travelmates.trips.explore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.trips.models.*
import cz.cvut.fit.travelmates.trips.TripsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.verify
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
class ExploreViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var tripsRepository: TripsRepository

    @MockK
    lateinit var searchTrips: SearchTripsUseCase

    private lateinit var viewModel: ExploreViewModel

    @MockK
    private lateinit var navigateTripDetailsObserver: Observer<Long>

    @MockK
    private lateinit var filteredTripsObserver: Observer<List<Trip>>

    @MockK
    private lateinit var contentObserver: Observer<Boolean>

    @MockK
    private lateinit var loadingObserver: Observer<Boolean>

    @MockK
    private lateinit var errorObserver: Observer<Boolean>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)

        coEvery { searchTrips.invoke(any(), any()) } returns listOf(trip2)
        viewModel = ExploreViewModel(tripsRepository, searchTrips)

        viewModel.eventNavigateTripDetails.observeForever(navigateTripDetailsObserver)
        viewModel.filteredTrips.observeForever(filteredTripsObserver)
        viewModel.contentVisible.observeForever(contentObserver)
        viewModel.loadingVisible.observeForever(loadingObserver)
        viewModel.errorVisible.observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when trips repository returns an error, error is visible`() {
        coEvery { tripsRepository.getExploreTrips() } throws IllegalArgumentException()

        verify(exactly = 1) { errorObserver.onChanged(true) }
    }

    @Test
    fun `when trips repository returns success, content is visible`() {
        coEvery { tripsRepository.getExploreTrips() } returns listOf(trip1)

        viewModel.onRetryPressed()

        verify(exactly = 1) { contentObserver.onChanged(true) }
        verify { filteredTripsObserver.onChanged(listOf(trip2)) }
    }

    @Test
    fun `when trip is pressed, correct event is triggered`() {
        coEvery { tripsRepository.getExploreTrips() } returns listOf(trip1)

        viewModel.onTripPressed(TEST_TRIP_ID)

        verify(exactly = 1) { navigateTripDetailsObserver.onChanged(TEST_TRIP_ID) }
    }

    companion object {
        private val trip1 = Trip(
            0,
            "title1",
            "",
            LocalDate.now(),
            TripState.GATHERING,
            Location(1.0, 2.0),
            listOf(
                TripRequirement(0, false, "reqa"),
                TripRequirement(1, false, "reqb"),
                TripRequirement(2, false, "reqc"),
            ),
            TripMember("", "", "", "", emptyList()),
            emptyList(),
            UserType.GUEST,
            emptyList(),
            null,
            emptyList()
        )
        private val trip2 = Trip(
            0,
            "eltit1",
            "",
            LocalDate.now(),
            TripState.GATHERING,
            Location(1.0, 2.0),
            listOf(
                TripRequirement(0, false, "qera"),
                TripRequirement(1, false, "qerb"),
                TripRequirement(2, false, "qerc"),
            ),
            TripMember("", "", "", "", emptyList()),
            emptyList(),
            UserType.GUEST,
            emptyList(),
            null,
            emptyList()
        )
        private const val TEST_TRIP_ID = 42L
    }
}