package cz.cvut.fit.travelmates.trips.addtrip

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.trips.models.Requirement
import cz.cvut.fit.travelmates.trips.addtrip.requirements.AddRequirementItem
import cz.cvut.fit.travelmates.trips.addtrip.requirements.RequirementItem
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class AddTripViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AddTripViewModel

    @MockK
    private lateinit var navigateContactStepObserver: Observer<PartialTrip>

    @MockK
    private lateinit var navigateAddRequirementObserver: Observer<Unit>

    @MockK
    private lateinit var navigatePickLocationObserver: Observer<Unit>

    @MockK
    private lateinit var navigatePickDateObserver: Observer<LocalDate>

    @MockK
    private lateinit var navigateBackObserver: Observer<Unit>

    @MockK
    private lateinit var createEnabledObserver: Observer<Boolean>

    @MockK
    private lateinit var locationObserver: Observer<Location>

    @MockK
    private lateinit var requirementsObserver: Observer<List<AddRequirementItem>>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)

        viewModel = AddTripViewModel()

        viewModel.eventNavigateAddRequirement.observeForever(navigateAddRequirementObserver)
        viewModel.eventPickDate.observeForever(navigatePickDateObserver)
        viewModel.eventNavigatePickLocation.observeForever(navigatePickLocationObserver)
        viewModel.eventNavigateBack.observeForever(navigateBackObserver)
        viewModel.eventNavigateContactStep.observeForever(navigateContactStepObserver)
        viewModel.isCreateEnabled.observeForever(createEnabledObserver)
        viewModel.location.observeForever(locationObserver)
        viewModel.requirementsUi.observeForever(requirementsObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `pressing add requirement triggers correct event`() {
        viewModel.onAddRequirementPressed()

        verify(exactly = 1) { navigateAddRequirementObserver.onChanged(null) }
    }

    @Test
    fun `when adding requirement, ui requirements are updated`() {
        viewModel.onRequirementAdded(TEST_REQUIREMENT.name)

        verify { requirementsObserver.onChanged(any()) }
    }

    @Test
    fun `when deleting requirement, ui requirements are updated`() {
        viewModel.onRequirementAdded(TEST_REQUIREMENT.name)
        viewModel.onDeleteRequirementPressed(RequirementItem("id", TEST_REQUIREMENT.name))

        verify(atLeast = 2) { requirementsObserver.onChanged(any()) }
    }

    @Test
    fun `when create is pressed, correct event is triggered`() {
        viewModel.typedTitle.value = TEST_TITLE
        viewModel.typedDescription.value = TEST_DESCRIPTION
        viewModel.onLocationPicked(TEST_LOCATION)
        viewModel.onDateSet(TEST_DATE)
        viewModel.onRequirementAdded(TEST_REQUIREMENT.name)
        val expectedTrip = PartialTrip(
            TEST_TITLE, TEST_DESCRIPTION, TEST_LOCATION, listOf(
                TEST_REQUIREMENT
            ), TEST_DATE
        )

        viewModel.onCreatePressed()

        verify(exactly = 1) { navigateContactStepObserver.onChanged(expectedTrip) }
    }

    @Test
    fun `when pick location is pressed, correct event is triggered`() {
        viewModel.onPickLocationPressed()

        verify(exactly = 1) { navigatePickLocationObserver.onChanged(null) }
    }

    @Test
    fun `when location is set, it is exposed correctly`() {
        viewModel.onLocationPicked(TEST_LOCATION)

        verify(exactly = 1) { locationObserver.onChanged(TEST_LOCATION) }
    }

    @Test
    fun `when change date is pressed, correct event is triggered`() {
        viewModel.onDateSet(TEST_DATE)

        viewModel.onChangeDatePressed()

        verify(exactly = 1) { navigatePickDateObserver.onChanged(TEST_DATE) }
    }

    @Test
    fun `when suggested date is set, it is exposed correctly`() = runBlockingTest {
        viewModel.onDateSet(TEST_DATE)

        assert(TEST_DATE == viewModel.suggestedDate.first())
    }

    @Test
    fun `pressing back button triggers correct event`() {
        viewModel.onBackPressed()

        verify(exactly = 1) { navigateBackObserver.onChanged(null) }
    }

    companion object {
        private val TEST_DATE = LocalDate.of(2022, 1, 1)
        private val TEST_LOCATION = Location(1.0, 2.0)
        private const val TEST_TITLE = "title"
        private const val TEST_DESCRIPTION = "description"
        private val TEST_REQUIREMENT = Requirement("req")
    }
}