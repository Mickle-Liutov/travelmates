package cz.cvut.fit.travelmates.location

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "lat")
    val lat: Int,
    @Json(name = "lon")
    val lon: Int
)