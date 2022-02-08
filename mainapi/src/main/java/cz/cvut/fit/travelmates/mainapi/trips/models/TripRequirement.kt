package cz.cvut.fit.travelmates.mainapi.trips.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TripRequirement(
    @Json(name = "id")
    val id: Int,
    @Json(name = "isFulfilled")
    val isFulfilled: Boolean,
    @Json(name = "name")
    val name: String
)