package cz.cvut.fit.travelmates.mainapi.posts

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * API for posts
 */
interface PostsService {

    @POST("posts")
    suspend fun createPost(@Body body: NewPost): Response<Unit>

    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>

}