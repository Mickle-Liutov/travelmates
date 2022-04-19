package cz.cvut.fit.travelmates.trips.mytrips

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.trips.models.*
import cz.cvut.fit.travelmates.mainapi.user.PublicUser
import cz.cvut.fit.travelmates.mainapi.user.User
import cz.cvut.fit.travelmates.mainapi.user.UserService
import cz.cvut.fit.travelmates.trips.TripsRepository
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTrip
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTripsSubtitle
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTripsSubtitleType
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.time.LocalDate

@ExperimentalCoroutinesApi
class ComposeMyTripsUseCaseTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var tripsRepository: TripsRepository

    @MockK
    lateinit var userService: UserService

    private lateinit var composeMyTripsUseCase: ComposeMyTripsUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)

        composeMyTripsUseCase = ComposeMyTripsUseCase(tripsRepository, userService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `trip items are composed correctly`() = runBlockingTest {
        val inputTrips = listOf(OWNER_TRIP, MEMBER_TRIP, REQUEST_TRIP)
        coEvery { userService.getUser() } returns Response.success(TEST_USER)
        coEvery { tripsRepository.getMyTrips() } returns inputTrips
        val expected = listOf(
            MyTripsSubtitle(MyTripsSubtitleType.OWNER),
            with(OWNER_TRIP) {
                MyTrip(description, id, location, owner, requirements, state, suggestedDate, title)
            },
            MyTripsSubtitle(MyTripsSubtitleType.REQUEST),
            with(REQUEST_TRIP) {
                MyTrip(description, id, location, owner, requirements, state, suggestedDate, title)
            },
            MyTripsSubtitle(MyTripsSubtitleType.MEMBER),
            with(MEMBER_TRIP) {
                MyTrip(description, id, location, owner, requirements, state, suggestedDate, title)
            },
        )

        val actual = composeMyTripsUseCase.invoke()

        assert(actual == expected)
    }

    companion object {
        private val TEST_USER = User("email", "", "")
        private const val OTHER_EMAIL = "other_email"
        private val OWNER_TRIP = Trip(
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
            TripMember(TEST_USER.email, "", "", "", emptyList()),
            emptyList(),
            UserType.GUEST,
            emptyList(),
            null,
            emptyList()
        )
        private val MEMBER_TRIP = Trip(
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
            TripMember(OTHER_EMAIL, "", "", "", emptyList()),
            listOf(TripMember(TEST_USER.email, "", "", "", emptyList())),
            UserType.GUEST,
            emptyList(),
            null,
            emptyList()
        )
        private val REQUEST_TRIP = Trip(
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
            TripMember(OTHER_EMAIL, "", "", "", emptyList()),
            emptyList(),
            UserType.GUEST,
            emptyList(),
            Request(1, PublicUser("", ""), emptyList(), "", "", RequestState.PENDING, ""),
            emptyList()
        )
    }
}