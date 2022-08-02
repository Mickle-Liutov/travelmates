package cz.cvut.fit.travelmates.trips.tripdetails

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.trips.models.*
import cz.cvut.fit.travelmates.mainapi.user.PublicUser
import cz.cvut.fit.travelmates.trips.TripsRepository
import cz.cvut.fit.travelmates.trips.tripdetails.images.UploadImageUseCase
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
class TripDetailsViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var tripsRepository: TripsRepository

    private val tripDetailsStateMapper = TripDetailsStateMapper()

    @MockK
    lateinit var uploadImage: UploadImageUseCase

    private lateinit var viewModel: TripDetailsViewModel

    @MockK
    private lateinit var tripObserver: Observer<Trip>

    @MockK
    lateinit var navigateJoinObserver: Observer<Trip>

    @MockK
    lateinit var navigateRequestObserver: Observer<Request>

    @MockK
    lateinit var pickImageObserver: Observer<Unit>

    @MockK
    lateinit var navigateMemberObserver: Observer<TripMember>

    @MockK
    lateinit var stopGatheringErrorObserver: Observer<Unit>

    @MockK
    lateinit var finishTripErrorObserver: Observer<Unit>

    @MockK
    lateinit var uploadImageErrorObserver: Observer<Unit>

    @MockK
    lateinit var navigateBackObserver: Observer<Unit>

    @MockK
    lateinit var mockedBitmap: Bitmap

    @MockK
    lateinit var lifecycleOwner: LifecycleOwner

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)

        coEvery { tripsRepository.getTripDetails(TRIP_ID) } returns TRIP_GUEST
        val savedStateHandle = SavedStateHandle(mapOf(KEY_TRIP_ID to TRIP_ID))
        viewModel = TripDetailsViewModel(
            savedStateHandle,
            tripsRepository,
            tripDetailsStateMapper,
            uploadImage
        )
        viewModel.detailedTrip.observeForever(tripObserver)
        viewModel.eventNavigateJoin.observeForever(navigateJoinObserver)
        viewModel.eventNavigateRequest.observeForever(navigateRequestObserver)
        viewModel.eventPickImage.observeForever(pickImageObserver)
        viewModel.eventNavigateMember.observeForever(navigateMemberObserver)
        viewModel.eventStopGatheringError.observeForever(stopGatheringErrorObserver)
        viewModel.eventFinishTripError.observeForever(finishTripErrorObserver)
        viewModel.eventUploadImageError.observeForever(uploadImageErrorObserver)
        viewModel.eventNavigateBack.observeForever(navigateBackObserver)

        viewModel.onResume(lifecycleOwner)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `pressing join triggers correct event`() {
        viewModel.onJoinPressed()

        verify { navigateJoinObserver.onChanged(TRIP_GUEST) }
    }

    @Test
    fun `pressing review request triggers correct event`() {
        viewModel.onReviewRequestPressed(REQUEST)

        verify { navigateRequestObserver.onChanged(REQUEST) }
    }

    @Test
    fun `when stop gathering fails, correct error is called`() {
        coEvery { tripsRepository.stopGatheringTrip(TRIP_ID) } throws IllegalArgumentException()

        viewModel.onStopGatheringPressed()

        verify { stopGatheringErrorObserver.onChanged(null) }
    }

    @Test
    fun `when finish trip fails, correct error is called`() {
        coEvery { tripsRepository.finishTrip(TRIP_ID) } throws IllegalArgumentException()

        viewModel.onFinishTripPressed()

        verify { finishTripErrorObserver.onChanged(null) }
    }

    @Test
    fun `when upload image is pressed, correct event is called`() {
        viewModel.onUploadImagePressed()

        verify { pickImageObserver.onChanged(null) }
    }

    @Test
    fun `when upload image fails, correct error is called`() {
        coEvery { uploadImage.invoke(any(), TRIP_ID) } throws IllegalArgumentException()

        viewModel.onImagePicked(mockedBitmap)

        verify { uploadImageErrorObserver.onChanged(null) }
    }

    @Test
    fun `pressing back button triggers correct event`() {
        viewModel.onBackPressed()

        verify { navigateBackObserver.onChanged(null) }
    }

    @Test
    fun `guest user is not able to open member details`() {
        viewModel.onMemberPressed(TEST_MEMBER)

        verify(exactly = 0) { navigateMemberObserver.onChanged(TEST_MEMBER) }
    }

    @Test
    fun `member user is able to open member details`() {
        coEvery { tripsRepository.getTripDetails(TRIP_ID) } returns TRIP_MEMBER

        viewModel.onRetryPressed()
        viewModel.onMemberPressed(TEST_MEMBER)

        verify(exactly = 1) { navigateMemberObserver.onChanged(TEST_MEMBER) }
    }

    companion object {
        private const val TRIP_ID = 2L
        private const val KEY_TRIP_ID = "tripId"
        private val TRIP_REQUIREMENTS = listOf(
            TripRequirement(0, false, "req1"),
            TripRequirement(1, false, "req2"),
            TripRequirement(2, false, "req3"),
        )
        private val TRIP_GUEST = Trip(
            TRIP_ID,
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
        private val TRIP_MEMBER = TRIP_GUEST.copy(userType = UserType.MEMBER)
        private val TEST_MEMBER = TripMember("email", "picture", "name", "contact", emptyList())
        private val REQUEST =
            Request(1, PublicUser("", ""), emptyList(), "", "", RequestState.PENDING, "")

    }
}