package cz.cvut.fit.travelmates.location.pick

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import cz.cvut.fit.travelmates.location.Location
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
class PickLocationViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: PickLocationViewModel

    @MockK
    private lateinit var navigateBackObserver: Observer<Unit>

    @MockK
    private lateinit var setResultObserver: Observer<Location>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = PickLocationViewModel()

        viewModel.eventNavigateBack.observeForever(navigateBackObserver)
        viewModel.eventSetResult.observeForever(setResultObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `back press triggers correct event`() {
        viewModel.onBackPressed()

        verify(exactly = 1) { navigateBackObserver.onChanged(null) }
    }

    @Test
    fun `without camera movement, pressing pick doesn't do anything`() {
        viewModel.onPickPressed()

        verify(exactly = 0) { setResultObserver.onChanged(any()) }
        verify(exactly = 0) { navigateBackObserver.onChanged(null) }
    }

    @Test
    fun `after camera moved, pressing pick triggers correct events`() {
        val location = Location(1.0, 2.0)
        val latLng = LatLng(location.lat, location.lon)

        viewModel.onCameraMoved(latLng)
        viewModel.onPickPressed()

        verify(exactly = 1) { setResultObserver.onChanged(location) }
        verify(exactly = 1) { navigateBackObserver.onChanged(null) }
    }

}