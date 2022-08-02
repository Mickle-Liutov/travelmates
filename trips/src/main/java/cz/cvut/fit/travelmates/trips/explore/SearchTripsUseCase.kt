package cz.cvut.fit.travelmates.trips.explore

import cz.cvut.fit.travelmates.mainapi.trips.models.Trip

/**
 * Use case for searching in trips with a term
 */
class SearchTripsUseCase {

    /**
     * Filters given trips using search term
     *
     * @param trips all trips
     * @param term search term
     * @return filtered trips
     */
    fun invoke(trips: List<Trip>, term: String): List<Trip> {
        return when {
            term.isBlank() -> trips
            else -> trips.filter {
                getSearchableText(it).contains(term, ignoreCase = true)
            }
        }
    }

    /**
     * Returns text, which should be used for searching for this trip
     */
    private fun getSearchableText(trip: Trip): String {
        return trip.title + " " + trip.requirements.joinToString(separator = " ") { it.name }
    }

}