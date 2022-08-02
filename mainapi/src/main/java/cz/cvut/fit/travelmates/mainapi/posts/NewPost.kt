package cz.cvut.fit.travelmates.mainapi.posts

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.cvut.fit.travelmates.location.Location

/**
 * DTO for creating new post
 */
@JsonClass(generateAdapter = true)
data class NewPost(
    @Json(name = "description")
    val description: String,
    @Json(name = "image")
    val image: String,
    @Json(name = "location")
    val location: Location
)
