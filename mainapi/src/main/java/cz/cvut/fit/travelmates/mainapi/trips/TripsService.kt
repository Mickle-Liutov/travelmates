package cz.cvut.fit.travelmates.mainapi.trips

import cz.cvut.fit.travelmates.mainapi.trips.models.NewTripDto
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TripsService {

    @GET(PATH_TRIPS)
    suspend fun getTrips(@Query(QUERY_FILTER) filter: String): Response<List<Trip>>

    @POST(PATH_TRIPS)
    suspend fun createTrip(@Body body: NewTripDto): Response<Unit>

    companion object {
        private const val PATH_TRIPS = "trips"
        private const val QUERY_FILTER = "filter"
        const val FILTER_EXPLORE = "explore"
        const val FILTER_MY_TRIPS = "mytrips"
    }
}