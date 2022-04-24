package cz.cvut.fit.travelmates.trips.mytrips

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.trips.models.TripMember
import cz.cvut.fit.travelmates.mainapi.trips.models.TripState
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTrip
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTripsItem
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTripsSubtitle
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTripsSubtitleType
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
class MyTripsViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var composeMyTripsUseCase: ComposeMyTripsUseCase

    private lateinit var viewModel: MyTripsViewModel

    @MockK
    private lateinit var navigateTripDetailsObserver: Observer<Long>

    @MockK
    private lateinit var tripsObserver: Observer<List<MyTripsItem>>

    @MockK
    private lateinit var contentObserver: Observer<Boolean>

    @MockK
    private lateinit var loadingObserver: Observer<Boolean>

    @MockK
    private lateinit var errorObserver: Observer<Boolean>

    @MockK
    private lateinit var lifecycleOwner: LifecycleOwner

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)

        viewModel = MyTripsViewModel(composeMyTripsUseCase)
        viewModel.onResume(lifecycleOwner)

        viewModel.eventNavigateDetails.observeForever(navigateTripDetailsObserver)
        viewModel.trips.observeForever(tripsObserver)
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
    fun `when trip is pressed, correct event is triggered`() {
        viewModel.onTripPressed(TEST_MY_TRIP)

        verify { navigateTripDetailsObserver.onChanged(TEST_MY_TRIP.id) }
    }

    @Test
    fun `when compose my trips returns an error, error state is set`() {
        coEvery { composeMyTripsUseCase.invoke() } throws IllegalArgumentException()

        viewModel.onRetryPressed()

        verify { errorObserver.onChanged(true) }
    }

    @Test
    fun `when compose my trips returns success, content is shown, items are exposed`() {
        coEvery { composeMyTripsUseCase.invoke() } returns TEST_MY_TRIP_ITEMS

        viewModel.onRetryPressed()

        verify { contentObserver.onChanged(true) }
        verify { tripsObserver.onChanged(TEST_MY_TRIP_ITEMS) }
    }

    companion object {
        private val TEST_MY_TRIP = MyTrip(
            "", 1, Location(1.0, 2.0), TripMember("", "", "", "", emptyList()),
            emptyList(), TripState.GATHERING, LocalDate.of(2022, 1, 1), ""
        )
        private val TEST_MY_TRIP_ITEMS = listOf(
            MyTripsSubtitle(MyTripsSubtitleType.OWNER),
            MyTripsSubtitle(MyTripsSubtitleType.MEMBER)
        )
    }
}