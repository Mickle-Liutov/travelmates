package cz.cvut.fit.travelmates.mainapi.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PublicUser(
    @Json(name = "name")
    val name: String,
    @Json(name = "picture")
    val picture: String?
)