package cz.cvut.fit.travelmates.posts

import cz.cvut.fit.travelmates.core.networking.toBody
import cz.cvut.fit.travelmates.mainapi.posts.NewPost
import cz.cvut.fit.travelmates.mainapi.posts.Post
import cz.cvut.fit.travelmates.mainapi.posts.PostsService

class PostsRepository(
    private val postsService: PostsService
) {

    /**
     * Creates a new post
     */
    suspend fun createPost(newPost: NewPost) {
        postsService.createPost(newPost).toBody()
    }

    /**
     * Gets a list of all posts
     */
    suspend fun getPosts(): List<Post> {
        return postsService.getPosts().toBody()
    }

}