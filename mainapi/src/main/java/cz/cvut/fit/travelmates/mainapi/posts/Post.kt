package cz.cvut.fit.travelmates.mainapi.posts

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.user.PublicUser


@JsonClass(generateAdapter = true)
data class Post(
    @Json(name = "createdAt")
    val createdAt: String,
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
