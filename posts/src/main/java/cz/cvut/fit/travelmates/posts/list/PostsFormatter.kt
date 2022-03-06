package cz.cvut.fit.travelmates.posts.list

import android.text.format.DateUtils
import cz.cvut.fit.travelmates.mainapi.posts.Post
import java.time.ZoneId

class PostsFormatter(post: Post) {
    val creatorIcon = post.creator.picture
    val creatorName = post.creator.name
    val createdAt = DateUtils.getRelativeTimeSpanString(
        post.createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )
    val description = post.description
    val image = post.image
}