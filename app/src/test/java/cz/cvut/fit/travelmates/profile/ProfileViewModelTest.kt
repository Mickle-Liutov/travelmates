package cz.cvut.fit.travelmates.profile

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import cz.cvut.fit.travelmates.authapi.AuthRepository
import cz.cvut.fit.travelmates.images.ImagesRepository
import cz.cvut.fit.travelmates.mainapi.user.User
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

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var imagesRepository: ImagesRepository

    @MockK
    lateinit var authRepository: AuthRepository

    private lateinit var viewModel: ProfileViewModel

    @MockK
    lateinit var saveErrorObserver: Observer<Unit>

    @MockK
    lateinit var changeImageErrorObserver: Observer<Unit>

    @MockK
    lateinit var logoutErrorObserver: Observer<Unit>

    @MockK
    lateinit var navigateBackObserver: Observer<Unit>

    @MockK
    lateinit var navigatePickImageObserver: Observer<Unit>

    @MockK
    lateinit var navigateMainObserver: Observer<Unit>

    @MockK
    lateinit var mockedBitmap: Bitmap

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)

        coEvery { userRepository.loadUser() } returns TEST_USER
        viewModel = ProfileViewModel(userRepository, imagesRepository, authRepository)

        viewModel.eventSaveError.observeForever(saveErrorObserver)
        viewModel.eventChangeImageError.observeForever(changeImageErrorObserver)
        viewModel.eventLogoutError.observeForever(logoutErrorObserver)
        viewModel.eventNavigateBack.observeForever(navigateBackObserver)
        viewModel.eventPickImage.observeForever(navigatePickImageObserver)
        viewModel.eventNavigateMain.observeForever(navigateMainObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when saving updated user fails, error event is called`() {
        coEvery { userRepository.updateUser(TEST_USER) } throws IllegalArgumentException()

        viewModel.onConfirmPressed()

        verify { saveErrorObserver.onChanged(null) }
    }


    @Test
    fun `when saving updated user is successful, error event is not called`() {
        coEvery { userRepository.updateUser(TEST_USER) } returns TEST_USER

        viewModel.onConfirmPressed()

        verify(exactly = 0) { saveErrorObserver.onChanged(null) }
    }

    @Test
    fun `pressing pick images triggers correct event`() {
        viewModel.onPickImagePressed()

        verify { navigatePickImageObserver.onChanged(null) }
    }

    @Test
    fun `when profile is updated successfully, error is not called`() {
        coEvery { imagesRepository.uploadImage(any(), any()) } returns TEST_USER.picture.orEmpty()
        coEvery { userRepository.updateUser(TEST_USER) } returns TEST_USER

        viewModel.onProfileImagePicked(mockedBitmap)

        verify(exactly = 0) { changeImageErrorObserver.onChanged(null) }
    }

    @Test
    fun `when profile image upload fails, correct event is called`() {
        coEvery { imagesRepository.uploadImage(any(), any()) } throws IllegalArgumentException()

        viewModel.onProfileImagePicked(mockedBitmap)

        verify { changeImageErrorObserver.onChanged(null) }
    }

    @Test
    fun `successful logout navigates user to main screen`() {
        viewModel.onLogoutPressed()

        verify(exactly = 1) { navigateMainObserver.onChanged(null) }
    }

    @Test
    fun `successful logout triggers correct event`() {
        coEvery { authRepository.logout() } throws IllegalArgumentException()

        viewModel.onLogoutPressed()

        verify(exactly = 0) { navigateMainObserver.onChanged(null) }
        verify(exactly = 1) { logoutErrorObserver.onChanged(null) }
    }

    @Test
    fun `when in edit mode, back button doesn't navigate back`() {
        viewModel.onEditPressed()

        viewModel.onBackPressed()

        verify(exactly = 0) { navigateBackObserver.onChanged(null) }
    }

    @Test
    fun `when in view mode, back button navigates back`() {
        viewModel.onBackPressed()

        verify(exactly = 1) { navigateBackObserver.onChanged(null) }
    }

    companion object {
        private val TEST_USER = User("email", "name", "picture")
    }
}