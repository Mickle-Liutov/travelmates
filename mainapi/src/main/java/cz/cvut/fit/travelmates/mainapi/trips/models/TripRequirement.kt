package cz.cvut.fit.travelmates.mainapi.trips.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class TripRequirement(
    @Json(name = "id")
    val id: Long,
    @Json(name = "isFulfilled")
    val isFulfilled: Boolean,
    @Json(name = "name")
    val name: String
) : Parcelable