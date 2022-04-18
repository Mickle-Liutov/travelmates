package cz.cvut.fit.travelmates.trips.explore

import cz.cvut.fit.travelmates.mainapi.trips.models.Trip

class SearchTripsUseCase {

    fun invoke(trips: List<Trip>, term: String): List<Trip> {
        return when {
            term.isBlank() -> trips
            else -> trips.filter {
                getSearchableText(it).contains(term, ignoreCase = true)
            }
        }
    }

    private fun getSearchableText(trip: Trip): String {
        return trip.title + " " + trip.requirements.joinToString(separator = " ") { it.name }
    }

}