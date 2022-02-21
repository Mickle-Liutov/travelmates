package cz.cvut.fit.travelmates.posts

import cz.cvut.fit.travelmates.mainapi.posts.Post

class PostsFormatter(post: Post) {
    val creatorIcon = post.creator.picture
    val creatorName = post.creator.name
    val createdAt = post.createdAt
    val description = post.description
    val image = post.image
}