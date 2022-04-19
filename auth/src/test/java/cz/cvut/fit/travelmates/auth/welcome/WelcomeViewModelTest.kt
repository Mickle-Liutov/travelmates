package cz.cvut.fit.travelmates.auth.welcome

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
class WelcomeViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: WelcomeViewModel

    @MockK
    private lateinit var navigateLoginObserver: Observer<Unit>

    @MockK
    private lateinit var navigateRegisterObserver: Observer<Unit>

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = WelcomeViewModel()

        viewModel.eventNavigateLogin.observeForever(navigateLoginObserver)
        viewModel.eventNavigateRegister.observeForever(navigateRegisterObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `login button triggers correct event`() {
        viewModel.onLoginPressed()

        verify(exactly = 1) { navigateLoginObserver.onChanged(null) }
    }

    @Test
    fun `register button triggers correct event`() {
        viewModel.onRegisterPressed()

        verify(exactly = 1) { navigateRegisterObserver.onChanged(null) }
    }
}