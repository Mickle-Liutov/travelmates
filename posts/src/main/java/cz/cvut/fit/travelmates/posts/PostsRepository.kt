package cz.cvut.fit.travelmates.posts

import cz.cvut.fit.travelmates.core.networking.toBody
import cz.cvut.fit.travelmates.mainapi.posts.NewPost
import cz.cvut.fit.travelmates.mainapi.posts.Post
import cz.cvut.fit.travelmates.mainapi.posts.PostsService

class PostsRepository(
    private val postsService: PostsService
) {

    suspend fun createPost(newPost: NewPost) {
        postsService.createPost(newPost).toBody()
    }

    suspend fun getPosts(): List<Post> {
        return postsService.getPosts().toBody()
    }

}