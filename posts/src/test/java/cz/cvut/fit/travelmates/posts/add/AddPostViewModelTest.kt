package cz.cvut.fit.travelmates.posts.add

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import cz.cvut.fit.travelmates.core.resources.ResourcesProvider
import cz.cvut.fit.travelmates.images.ImagesRepository
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.posts.NewPost
import cz.cvut.fit.travelmates.posts.PostsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
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
class AddPostViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AddPostViewModel

    @MockK
    lateinit var postsRepository: PostsRepository

    @MockK
    lateinit var imagesRepository: ImagesRepository

    @MockK
    lateinit var resourcesProvider: ResourcesProvider

    @MockK
    private lateinit var navigateBackObserver: Observer<Unit>

    @MockK
    private lateinit var errorObserver: Observer<String>

    @MockK
    private lateinit var navigateSetLocationObserver: Observer<Unit>

    @MockK
    private lateinit var navigatePickImageObserver: Observer<Unit>

    @MockK
    private lateinit var imageObserver: Observer<Bitmap>

    @MockK
    private lateinit var locationObserver: Observer<Location?>

    @MockK
    private lateinit var postVisibleObserver: Observer<Boolean>

    @MockK
    private lateinit var loadingVisibleObserver: Observer<Boolean>

    @MockK
    private lateinit var mockBitmap: Bitmap

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = AddPostViewModel(postsRepository, imagesRepository, resourcesProvider)
        every { resourcesProvider.getString(any()) } returns STRING_STUB

        viewModel.eventNavigateBack.observeForever(navigateBackObserver)
        viewModel.eventError.observeForever(errorObserver)
        viewModel.eventNavigateSetLocation.observeForever(navigateSetLocationObserver)
        viewModel.eventPickImage.observeForever(navigatePickImageObserver)
        viewModel.image.observeForever(imageObserver)
        viewModel.location.observeForever(locationObserver)
        viewModel.postVisible.observeForever(postVisibleObserver)
        viewModel.loadingVisible.observeForever(loadingVisibleObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `pressing upload image triggers correct event`() {
        viewModel.onUploadImagePressed()

        verify(exactly = 1) { navigatePickImageObserver.onChanged(null) }
    }

    @Test
    fun `setting picked image exposes it in livedata`() {
        viewModel.onImagePicked(mockBitmap)

        verify(exactly = 1) { imageObserver.onChanged(mockBitmap) }
    }

    @Test
    fun `pressing set location triggers correct event`() {
        viewModel.onSetLocationPressed()

        verify(exactly = 1) { navigateSetLocationObserver.onChanged(null) }
    }

    @Test
    fun `picking location exposes it in livedata`() {
        viewModel.onLocationPicked(TEST_LOCATION)

        verify(exactly = 1) { locationObserver.onChanged(TEST_LOCATION) }
    }

    @Test
    fun `pressing back button triggers correct event`() {
        viewModel.onBackPressed()

        verify(exactly = 1) { navigateBackObserver.onChanged(null) }
    }

    @Test
    fun `post button is not visible when location or image are not set`() {
        viewModel.onImagePicked(mockBitmap)

        verify { postVisibleObserver.onChanged(false) }
    }

    @Test
    fun `post button is visible when location and image are set`() {
        setValidArguments()

        verify { postVisibleObserver.onChanged(true) }
    }

    @Test
    fun `when image upload fails, error event is called`() {
        coEvery {
            imagesRepository.uploadImage(
                mockBitmap,
                any()
            )
        } throws IllegalArgumentException()
        setValidArguments()

        viewModel.onPostPressed()

        verify(exactly = 1) { errorObserver.onChanged(any()) }
    }

    @Test
    fun `when creating post fails, error event is called`() {
        val newPost = NewPost("", TEST_IMAGE_REF, TEST_LOCATION)
        coEvery {
            imagesRepository.uploadImage(
                mockBitmap,
                any()
            )
        } returns TEST_IMAGE_REF
        coEvery { postsRepository.createPost(newPost) } throws IllegalArgumentException()
        setValidArguments()

        viewModel.onPostPressed()

        verify(exactly = 1) { errorObserver.onChanged(any()) }
    }

    @Test
    fun `when creating post is successful, correct events are called`() {
        coEvery {
            imagesRepository.uploadImage(
                mockBitmap,
                any()
            )
        } returns TEST_IMAGE_REF
        setValidArguments()

        viewModel.onPostPressed()

        verify(exactly = 1) { navigateBackObserver.onChanged(null) }
    }

    private fun setValidArguments() {
        viewModel.onImagePicked(mockBitmap)
        viewModel.onLocationPicked(TEST_LOCATION)
    }

    companion object {
        private const val STRING_STUB = "stub"
        private val TEST_LOCATION = Location(1.0, 2.0)
        private val TEST_IMAGE_REF = "image_ref"
    }
}