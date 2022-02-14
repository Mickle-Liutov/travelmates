package cz.cvut.fit.travelmates.mainapi.trips.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewJoinRequestDto(
    @Json(name = "message")
    val message: String,
    @Json(name = "contact")
    val contact: String,
    @Json(name = "providedEquipmentIds")
    val providedEquipmentIds: List<Long>
)

