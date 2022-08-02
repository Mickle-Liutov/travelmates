package cz.cvut.fit.travelmates.trips.mytrips.list

import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.trips.models.TripMember
import cz.cvut.fit.travelmates.mainapi.trips.models.TripRequirement
import cz.cvut.fit.travelmates.mainapi.trips.models.TripState
import java.time.LocalDate

/**
 * Item in a list of my trips
 */
sealed class MyTripsItem {
    abstract fun areItemsSame(other: MyTripsItem): Boolean
    abstract fun areContentsSame(other: MyTripsItem): Boolean
    abstract val viewType: Int

    companion object {
        const val TYPE_ITEM = 1
        const val TYPE_SUBTITLE = 2
    }
}

/**
 * Trip item in list of my trips
 */
data class MyTrip(
    val description: String,
    val id: Long,
    val location: Location,
    val owner: TripMember,
    val requirements: List<TripRequirement>,
    val state: TripState,
    val suggestedTime: LocalDate,
    val title: String
) : MyTripsItem() {
    override fun areItemsSame(other: MyTripsItem): Boolean {
        return other is MyTrip && id == other.id
    }

    override fun areContentsSame(other: MyTripsItem): Boolean {
        return this == other
    }

    override val viewType = TYPE_ITEM
}

/**
 * Section subtitle item in list of my trips
 */
data class MyTripsSubtitle(val type: MyTripsSubtitleType) : MyTripsItem() {
    override fun areItemsSame(other: MyTripsItem): Boolean {
        return this == other
    }

    override fun areContentsSame(other: MyTripsItem): Boolean {
        return this == other
    }

    override val viewType = TYPE_SUBTITLE
}