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

    suspend fun getExploreTrips(): List<Trip> {
        return tripsService.getTrips(TripsService.FILTER_EXPLORE).toBody()
    }

    suspend fun getMyTrips(): List<Trip> {
        return tripsService.getTrips(TripsService.FILTER_MY_TRIPS).toBody()
    }

    suspend fun createTrip(newTrip: NewTripDto) {
        return tripsService.createTrip(newTrip).toBody()
    }

    suspend fun getTripDetails(tripId: Long): Trip {
        return tripsService.getTripDetails(tripId).toBody()
    }

    suspend fun sendJoinRequest(tripId: Long, newJoinRequestDto: NewJoinRequestDto) {
        tripsService.sendJoinRequest(tripId, newJoinRequestDto)
    }

    suspend fun stopGatheringTrip(tripId: Long) {
        tripsService.stopGatheringTrip(tripId)
    }

    suspend fun finishTrip(tripId: Long) {
        tripsService.finishTrip(tripId)
    }

    suspend fun uploadTripImage(tripId: Long, imageRef: String) {
        tripsService.uploadTripImage(tripId, UploadImageDto(imageRef))
    }

}