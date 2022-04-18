package cz.cvut.fit.travelmates.mainapi.trips.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.cvut.fit.travelmates.location.Location
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class Trip(
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "suggestedDate")
    val suggestedDate: LocalDate,
    @Json(name = "state")
    val state: TripState,
    @Json(name = "location")
    val location: Location,
    @Json(name = "requirements")
    val requirements: List<TripRequirement>,
    @Json(name = "owner")
    val owner: TripMember,
    @Json(name = "members")
    val members: List<TripMember>,
    @Json(name = "currentUserType")
    val userType: UserType,
    @Json(name = "requests")
    val requests: List<Request>?,
    @Json(name = "currentUserRequest")
    val currentUserRequest: Request?,
    @Json(name = "images")
    val images: List<String>
) : Parcelable

object TripDiff : DiffUtil.ItemCallback<Trip>() {
    override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean {
        return oldItem == newItem
    }
}
