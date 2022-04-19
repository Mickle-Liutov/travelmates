package cz.cvut.fit.travelmates.posts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.posts.NewPost
import cz.cvut.fit.travelmates.mainapi.posts.Post
import cz.cvut.fit.travelmates.mainapi.posts.PostsService
import cz.cvut.fit.travelmates.mainapi.user.PublicUser
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class PostsRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var postsService: PostsService

    private lateinit var postsRepository: PostsRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        postsRepository = PostsRepository(postsService)
    }

    @Test
    fun `creating post calls correct method of service`() = runBlockingTest {
        val newPost = NewPost("", "", Location(1.0, 2.0))
        coEvery { postsService.createPost(newPost) } returns Response.success(Unit)

        postsRepository.createPost(newPost)
    }

    @Test
    fun `getting posts returns posts from service`() = runBlockingTest {
        val postsList = listOf(
            TEST_POST, TEST_POST, TEST_POST
        )
        coEvery { postsService.getPosts() } returns Response.success(postsList)

        postsRepository.getPosts()
    }

    companion object {
        private val TEST_POST = Post(
            LocalDateTime.of(2022, 1, 1, 1, 1), PublicUser("", ""), "", 1, "",
            Location(1.0, 2.0)
        )
    }
}