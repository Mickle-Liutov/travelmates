package cz.cvut.fit.travelmates.mainapi.trips.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//TODO Add fields
@JsonClass(generateAdapter = true)
data class NewTripDto(
    @Json(name = "title")
    val title: String
)