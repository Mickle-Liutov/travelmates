package cz.cvut.fit.travelmates.trips

import cz.cvut.fit.travelmates.core.networking.toBody
import cz.cvut.fit.travelmates.mainapi.trips.TripsService
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip

class TripsRepository(
    private val tripsService: TripsService
) {

    suspend fun getExploreTrips(): List<Trip> {
        return tripsService.getTrips(TripsService.FILTER_EXPLORE).toBody()
    }

    suspend fun getMyTrips(): List<Trip> {
        return tripsService.getTrips(TripsService.FILTER_MY_TRIPS).toBody()
    }

}