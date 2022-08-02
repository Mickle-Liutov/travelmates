package cz.cvut.fit.travelmates.trips.member

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import cz.cvut.fit.travelmates.mainapi.trips.models.TripMember
import io.mockk.MockKAnnotations
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

@ExperimentalCoroutinesApi
class TripMemberViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TripMemberViewModel

    @MockK
    private lateinit var imageObserver: Observer<String?>

    @MockK
    private lateinit var nameObserver: Observer<String>

    @MockK
    private lateinit var contactObserver: Observer<String>

    @MockK
    private lateinit var navigateBackObserver: Observer<Unit>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)

        val savedStateHandle = SavedStateHandle(mapOf(TRIP_MEMBER_KEY to TRIP_MEMBER))
        viewModel = TripMemberViewModel(savedStateHandle)

        viewModel.userImage.observeForever(imageObserver)
        viewModel.userName.observeForever(nameObserver)
        viewModel.userContact.observeForever(contactObserver)
        viewModel.eventNavigateBack.observeForever(navigateBackObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `correct user information is set immediately`() {
        verify { nameObserver.onChanged(TRIP_MEMBER.name) }
        verify { contactObserver.onChanged(TRIP_MEMBER.contact) }
        verify { imageObserver.onChanged(TRIP_MEMBER.picture) }
    }

    @Test
    fun `pressing back button triggers correct event`() {
        viewModel.onBackPressed()

        verify { navigateBackObserver.onChanged(null) }
    }

    companion object {
        private val TRIP_MEMBER = TripMember("email", "picture", "name", "contact", emptyList())
        private const val TRIP_MEMBER_KEY = "tripMember"
    }
}
