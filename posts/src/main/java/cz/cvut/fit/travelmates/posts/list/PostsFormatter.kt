package cz.cvut.fit.travelmates.posts.list

import android.text.format.DateUtils
import cz.cvut.fit.travelmates.mainapi.posts.Post
import java.time.ZoneId

/**
 * Formatter for Post item
 */
class PostsFormatter(post: Post) {
    val creatorIcon = post.creator.picture
    val creatorName = post.creator.name

    //Get string of how long ago post was created
    val createdAt: String = DateUtils.getRelativeTimeSpanString(
        post.createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )?.toString().orEmpty()
    val description = post.description
    val image = post.image
}