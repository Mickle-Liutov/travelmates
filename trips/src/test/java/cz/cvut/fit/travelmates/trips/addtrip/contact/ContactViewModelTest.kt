package cz.cvut.fit.travelmates.trips.addtrip.contact

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.trips.models.NewTripDto
import cz.cvut.fit.travelmates.mainapi.trips.models.Requirement
import cz.cvut.fit.travelmates.trips.TripsRepository
import cz.cvut.fit.travelmates.trips.addtrip.PartialTrip
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
class ContactViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var tripsRepository: TripsRepository

    private lateinit var viewModel: ContactViewModel

    @MockK
    private lateinit var navigateMainObserver: Observer<Unit>

    @MockK
    private lateinit var navigateBackObserver: Observer<Unit>

    @MockK
    private lateinit var tripCreatedObserver: Observer<Unit>

    @MockK
    private lateinit var contentVisibleObserver: Observer<Boolean>

    @MockK
    private lateinit var loadingVisibleObserver: Observer<Boolean>

    @MockK
    private lateinit var errorObserver: Observer<Unit>

    @MockK
    private lateinit var createEnabledObserver: Observer<Boolean>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)

        val savedStateHandle = SavedStateHandle(mapOf(KEY_PARTIAL_TRIP to PARTIAL_TRIP))
        viewModel = ContactViewModel(tripsRepository, savedStateHandle)

        viewModel.eventNavigateMain.observeForever(navigateMainObserver)
        viewModel.eventNavigateBack.observeForever(navigateBackObserver)
        viewModel.contentVisible.observeForever(contentVisibleObserver)
        viewModel.loadingVisible.observeForever(loadingVisibleObserver)
        viewModel.eventError.observeForever(errorObserver)
        viewModel.eventShowTripCreated.observeForever(tripCreatedObserver)
        viewModel.isCreateEnabled.observeForever(createEnabledObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when contact is blank, create button is disabled`() {
        viewModel.typedContact.value = EMPTY_FIELD

        verify { createEnabledObserver.onChanged(false) }
    }

    @Test
    fun `pressing back button triggers correct event`() {
        viewModel.onBackPressed()

        verify { navigateBackObserver.onChanged(null) }
    }

    @Test
    fun `when creating trip fails, error is called`() {
        val newTrip = with(PARTIAL_TRIP) {
            NewTripDto(title, description, location, CONTACT, requirements, suggestedDate)
        }
        coEvery { tripsRepository.createTrip(newTrip) } throws IllegalArgumentException()
        viewModel.typedContact.value = CONTACT

        viewModel.onCreatePressed()

        verify(exactly = 1) { errorObserver.onChanged(null) }
        verifyLoadingStateSequence()
    }

    @Test
    fun `when creating trip is successful, correct events are called`() {
        viewModel.typedContact.value = CONTACT

        viewModel.onCreatePressed()

        verify(exactly = 0) { errorObserver.onChanged(null) }
        verify(exactly = 1) { tripCreatedObserver.onChanged(null) }
        verify(exactly = 1) { navigateMainObserver.onChanged(null) }
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
        private const val KEY_PARTIAL_TRIP = "partialTrip"
        private val PARTIAL_TRIP = PartialTrip(
            "", "", Location(1.0, 2.0), listOf(
                Requirement(""),
                Requirement("")
            ), LocalDate.of(2022, 1, 1)
        )
        private const val EMPTY_FIELD = ""
        private const val CONTACT = "contact"
    }
}