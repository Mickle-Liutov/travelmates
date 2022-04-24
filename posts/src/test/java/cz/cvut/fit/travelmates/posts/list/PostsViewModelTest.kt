package cz.cvut.fit.travelmates.posts.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.posts.Post
import cz.cvut.fit.travelmates.mainapi.user.PublicUser
import cz.cvut.fit.travelmates.posts.PostsRepository
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
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class PostsViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: PostsViewModel

    @MockK
    lateinit var postsRepository: PostsRepository

    @MockK
    private lateinit var navigateAddPostObserver: Observer<Unit>

    @MockK
    private lateinit var contentVisibleObserver: Observer<Boolean>

    @MockK
    private lateinit var loadingVisibleObserver: Observer<Boolean>

    @MockK
    private lateinit var errorVisibleObserver: Observer<Boolean>

    @MockK
    private lateinit var postsObserver: Observer<List<Post>>

    @MockK
    private lateinit var lifecycleOwner: LifecycleOwner

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true)
        coEvery { postsRepository.getPosts() } returns emptyList()
        viewModel = PostsViewModel(postsRepository)
        viewModel.onResume(lifecycleOwner)

        viewModel.eventNavigateAddPost.observeForever(navigateAddPostObserver)
        viewModel.contentVisible.observeForever(contentVisibleObserver)
        viewModel.loadingVisible.observeForever(loadingVisibleObserver)
        viewModel.errorVisible.observeForever(errorVisibleObserver)
        viewModel.posts.observeForever(postsObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `pressing add post triggers correct event`() {
        viewModel.onAddPressed()

        verify(exactly = 1) { navigateAddPostObserver.onChanged(null) }
    }

    @Test
    fun `when posts repository returns error, error is visible`() {
        coEvery { postsRepository.getPosts() } throws IllegalArgumentException()

        viewModel.onRetryPressed()

        verify(exactly = 1) { errorVisibleObserver.onChanged(true) }
    }

    @Test
    fun `when posts repository returns posts, content is visible`() {
        val testPosts =
            listOf(
                Post(LocalDateTime.now(), PublicUser("", ""), "", 1, "", Location(1.0, 2.0))
            )
        coEvery { postsRepository.getPosts() } returns testPosts

        viewModel.onRetryPressed()

        verifySequence {
            contentVisibleObserver.onChanged(true)
            contentVisibleObserver.onChanged(false)
            contentVisibleObserver.onChanged(true)
        }
        verify(exactly = 1) { postsObserver.onChanged(testPosts) }
    }
}