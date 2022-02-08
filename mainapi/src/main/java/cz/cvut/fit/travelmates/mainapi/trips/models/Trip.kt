package cz.cvut.fit.travelmates.mainapi.trips.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.user.PublicUser


@JsonClass(generateAdapter = true)
data class Trip(
    @Json(name = "description")
    val description: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "location")
    val location: Location,
    @Json(name = "owner")
    val owner: PublicUser,
    @Json(name = "requirements")
    val requirements: List<TripRequirement>,
    @Json(name = "state")
    val state: String,
    @Json(name = "suggestedTime")
    val suggestedTime: String,
    @Json(name = "title")
    val title: String
)


