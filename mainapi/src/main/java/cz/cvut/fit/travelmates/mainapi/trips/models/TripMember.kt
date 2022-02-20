package cz.cvut.fit.travelmates.mainapi.trips.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class TripMember(
    @Json(name = "email")
    val email: String,
    @Json(name = "picture")
    val picture: String?,
    @Json(name = "name")
    val name: String,
    @Json(name = "contact")
    val contact: String,
    @Json(name = "providedEquipment")
    val providedEquipment: List<TripRequirement>
) : Parcelable