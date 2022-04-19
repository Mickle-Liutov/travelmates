package cz.cvut.fit.travelmates.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import cz.cvut.fit.travelmates.authapi.AuthRepository
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.posts.Post
import cz.cvut.fit.travelmates.mainapi.trips.models.*
import cz.cvut.fit.travelmates.mainapi.user.PublicUser
import cz.cvut.fit.travelmates.posts.PostsRepository
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
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var authRepository: AuthRepository

    @MockK
    lateinit var tripsRepository: TripsRepository

    @MockK
    lateinit var postsRepository: PostsRepository

    private lateinit var viewModel: HomeViewModel

    @MockK
    lateinit var tripsObserver: Observer<List<Trip>>

    @MockK
    lateinit var postsObserver: Observer<List<Post>>

    @MockK
    lateinit var navigateTripsObserver: Observer<Unit>

    @MockK
    lateinit var navigatePostsObserver: Observer<Unit>

    @MockK
    lateinit var navigateOrganizeObserver: Observer<Unit>

    @MockK
    lateinit var navigateAuthObserver: Observer<Unit>

    @MockK
    lateinit var navigateTripDetailsObserver: Observer<Long>

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

        viewModel = HomeViewModel(authRepository, tripsRepository, postsRepository)

        viewModel.eventNavigateTrips.observeForever(navigateTripsObserver)
        viewModel.eventNavigatePosts.observeForever(navigatePostsObserver)
        viewModel.eventNavigateOrganize.observeForever(navigateOrganizeObserver)
        viewModel.eventNavigateAuth.observeForever(navigateAuthObserver)
        viewModel.eventNavigateTripDetails.observeForever(navigateTripDetailsObserver)
        viewModel.contentVisible.observeForever(contentObserver)
        viewModel.loadingVisible.observeForever(loadingObserver)
        viewModel.errorVisible.observeForever(errorObserver)
        viewModel.posts.observeForever(postsObserver)
        viewModel.trips.observeForever(tripsObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when user is not authorized, correct event is called`() {
        coEvery { authRepository.hasValidSession() } returns false

        viewModel.onResume(lifecycleOwner)

        verify { navigateAuthObserver.onChanged(null) }
    }

    @Test
    fun `when user is authorized, data is loaded`() {
        coEvery { authRepository.hasValidSession() } returns true
        coEvery { postsRepository.getPosts() } returns listOf(POST)
        coEvery { tripsRepository.getExploreTrips() } returns listOf(TRIP)

        viewModel.onResume(lifecycleOwner)

        verify { postsObserver.onChanged(listOf(POST)) }
        verify { tripsObserver.onChanged(listOf(TRIP)) }
        verifySequence {
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }
        verifySequence {
            contentObserver.onChanged(false)
            contentObserver.onChanged(true)
        }
    }

    @Test
    fun `when error is returned when loading data, error state is shown`() {
        coEvery { authRepository.hasValidSession() } returns true
        coEvery { postsRepository.getPosts() } returns listOf(POST)
        coEvery { tripsRepository.getExploreTrips() } throws IllegalArgumentException()

        viewModel.onResume(lifecycleOwner)

        verifySequence {
            errorObserver.onChanged(false)
            errorObserver.onChanged(true)
        }
    }

    @Test
    fun `when see all trips is pressed, correct event is triggered`() {
        viewModel.onSeeAllTripsPressed()

        verify { navigateTripsObserver.onChanged(null) }
    }

    @Test
    fun `when see all posts is pressed, correct event is triggered`() {
        viewModel.onSeeAllPostsPressed()

        verify { navigatePostsObserver.onChanged(null) }
    }

    @Test
    fun `when organize is pressed, correct event is triggered`() {
        viewModel.onOrganizePressed()

        verify { navigateOrganizeObserver.onChanged(null) }
    }

    @Test
    fun `when trip is pressed, correct event is triggered`() {
        viewModel.onTripPressed(TRIP)

        verify { navigateTripDetailsObserver.onChanged(TRIP.id) }
    }

    companion object {
        private val TRIP = Trip(
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
        private val POST = Post(
            LocalDateTime.of(2022, 1, 1, 1, 1),
            PublicUser("", ""), "", 1, "", Location(1.0, 2.0)
        )
    }
}