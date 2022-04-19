package cz.cvut.fit.travelmates.trips

import cz.cvut.fit.travelmates.core.networking.toBody
import cz.cvut.fit.travelmates.mainapi.trips.TripsService
import cz.cvut.fit.travelmates.mainapi.trips.models.NewJoinRequestDto
import cz.cvut.fit.travelmates.mainapi.trips.models.NewTripDto
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip
import cz.cvut.fit.travelmates.mainapi.trips.models.UploadImageDto

class TripsRepository(
    private val tripsService: TripsService
) {

    /**
     * Get a list of explore trips
     */
    suspend fun getExploreTrips(): List<Trip> {
        return tripsService.getTrips(TripsService.FILTER_EXPLORE).toBody()
    }

    /**
     * Get a list of my trips
     */
    suspend fun getMyTrips(): List<Trip> {
        return tripsService.getTrips(TripsService.FILTER_MY_TRIPS).toBody()
    }

    /**
     * Create a new trip
     */
    suspend fun createTrip(newTrip: NewTripDto) {
        return tripsService.createTrip(newTrip).toBody()
    }

    /**
     * Get a trip with given id
     */
    suspend fun getTripDetails(tripId: Long): Trip {
        return tripsService.getTripDetails(tripId).toBody()
    }

    /**
     * Send a join request to a trip
     */
    suspend fun sendJoinRequest(tripId: Long, newJoinRequestDto: NewJoinRequestDto) {
        tripsService.sendJoinRequest(tripId, newJoinRequestDto)
    }

    /**
     * Stop gathering a trip
     */
    suspend fun stopGatheringTrip(tripId: Long) {
        tripsService.stopGatheringTrip(tripId)
    }

    /**
     * Finish a trip
     */
    suspend fun finishTrip(tripId: Long) {
        tripsService.finishTrip(tripId)
    }

    /**
     * Upload an image for a trip
     */
    suspend fun uploadTripImage(tripId: Long, imageRef: String) {
        tripsService.uploadTripImage(tripId, UploadImageDto(imageRef))
    }

}