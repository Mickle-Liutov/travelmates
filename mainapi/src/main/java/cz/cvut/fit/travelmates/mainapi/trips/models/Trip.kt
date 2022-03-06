package cz.cvut.fit.travelmates.mainapi.trips.models

import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.user.PublicUser


@JsonClass(generateAdapter = true)
data class Trip(
    @Json(name = "description")
    val description: String,
    @Json(name = "id")
    val id: Long,
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

object TripDiff : DiffUtil.ItemCallback<Trip>() {
    override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean {
        return oldItem == newItem
    }
}


