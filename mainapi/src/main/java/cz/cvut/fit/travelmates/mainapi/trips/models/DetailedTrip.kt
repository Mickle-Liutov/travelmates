package cz.cvut.fit.travelmates.mainapi.trips.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.cvut.fit.travelmates.location.Location
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class DetailedTrip(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "suggestedDate")
    val suggestedDate: String,
    @Json(name = "state")
    val state: TripState,
    @Json(name = "location")
    val location: Location,
    @Json(name = "pendingRequirements")
    val pendingRequirements: List<TripRequirement>,
    @Json(name = "owner")
    val owner: TripMember,
    @Json(name = "members")
    val members: List<TripMember>,
    @Json(name = "currentUserType")
    val userType: UserType,
    @Json(name = "requests")
    val requests: List<Request>?
) : Parcelable
