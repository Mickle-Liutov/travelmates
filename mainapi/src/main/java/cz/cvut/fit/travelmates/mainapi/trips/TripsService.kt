package cz.cvut.fit.travelmates.mainapi.trips

import cz.cvut.fit.travelmates.mainapi.trips.models.DetailedTrip
import cz.cvut.fit.travelmates.mainapi.trips.models.NewTripDto
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip
import retrofit2.Response
import retrofit2.http.*

interface TripsService {

    @GET(PATH_TRIPS)
    suspend fun getTrips(@Query(QUERY_FILTER) filter: String): Response<List<Trip>>

    @POST(PATH_TRIPS)
    suspend fun createTrip(@Body body: NewTripDto): Response<Unit>

    @GET("$PATH_TRIPS/{$PATH_TRIP_ID}")
    suspend fun getTripDetails(@Path(PATH_TRIP_ID) tripId: Long): Response<DetailedTrip>

    companion object {
        private const val PATH_TRIPS = "trips"
        private const val PATH_TRIP_ID = "tripId"
        private const val QUERY_FILTER = "filter"
        const val FILTER_EXPLORE = "explore"
        const val FILTER_MY_TRIPS = "mytrips"
    }
}