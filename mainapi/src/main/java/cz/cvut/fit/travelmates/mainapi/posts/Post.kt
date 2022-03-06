package cz.cvut.fit.travelmates.mainapi.posts

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.user.PublicUser
import java.time.LocalDateTime


@JsonClass(generateAdapter = true)
data class Post(
    @Json(name = "createdAt")
    val createdAt: LocalDateTime,
    @Json(name = "creator")
    val creator: PublicUser,
    @Json(name = "description")
    val description: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "image")
    val image: String,
    @Json(name = "location")
    val location: Location
)

object PostDiff : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}
