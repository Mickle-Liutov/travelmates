package cz.cvut.fit.travelmates.trips.addtrip.requirements

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
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
class AddRequirementDialogViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AddRequirementDialogViewModel

    @MockK
    private lateinit var setArgumentObserver: Observer<String>

    @MockK
    private lateinit var navigateBackObserver: Observer<Unit>

    @MockK
    private lateinit var addEnabledObserver: Observer<Boolean>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = AddRequirementDialogViewModel()

        viewModel.eventSetArgument.observeForever(setArgumentObserver)
        viewModel.eventNavigateBack.observeForever(navigateBackObserver)
        viewModel.isAddEnabled.observeForever(addEnabledObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when name is blank, add button is disabled`() {
        viewModel.typedName.value = EMPTY_FIELD

        verify { addEnabledObserver.onChanged(false) }
    }

    @Test
    fun `when name is not blank, add button is disabled`() {
        viewModel.typedName.value = NAME

        verify { addEnabledObserver.onChanged(true) }
    }

    @Test
    fun `when add is pressed, correct events are called`() {
        viewModel.typedName.value = NAME

        viewModel.onAddPressed()

        verify(exactly = 1) { setArgumentObserver.onChanged(NAME) }
        verify(exactly = 1) { navigateBackObserver.onChanged(null) }
    }

    companion object {
        private const val EMPTY_FIELD = ""
        private const val NAME = "boat"
    }
}