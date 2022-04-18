package cz.cvut.fit.travelmates.mainapi.trips

import cz.cvut.fit.travelmates.mainapi.trips.models.NewJoinRequestDto
import cz.cvut.fit.travelmates.mainapi.trips.models.NewTripDto
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip
import cz.cvut.fit.travelmates.mainapi.trips.models.UploadImageDto
import retrofit2.Response
import retrofit2.http.*

interface TripsService {

    @GET(PATH_TRIPS)
    suspend fun getTrips(@Query(QUERY_FILTER) filter: String): Response<List<Trip>>

    @POST(PATH_TRIPS)
    suspend fun createTrip(@Body body: NewTripDto): Response<Unit>

    @GET("$PATH_TRIPS/{$PATH_TRIP_ID}")
    suspend fun getTripDetails(@Path(PATH_TRIP_ID) tripId: Long): Response<Trip>

    @POST("${PATH_TRIPS}/{${PATH_TRIP_ID}}/join")
    suspend fun sendJoinRequest(
        @Path(PATH_TRIP_ID) tripId: Long,
        @Body body: NewJoinRequestDto
    ): Response<Unit>

    @PATCH("$PATH_TRIPS/{${PATH_TRIP_ID}}/stopGathering")
    suspend fun stopGatheringTrip(@Path(PATH_TRIP_ID) tripId: Long): Response<Unit>

    @PATCH("$PATH_TRIPS/{${PATH_TRIP_ID}}/finish")
    suspend fun finishTrip(@Path(PATH_TRIP_ID) tripId: Long): Response<Unit>

    @POST("$PATH_TRIPS/{${PATH_TRIP_ID}}/images")
    suspend fun uploadTripImage(
        @Path(PATH_TRIP_ID) tripId: Long,
        @Body uploadImageDto: UploadImageDto
    ): Response<Unit>

    companion object {
        private const val PATH_TRIPS = "trips"
        private const val PATH_TRIP_ID = "tripId"
        private const val QUERY_FILTER = "filter"
        const val FILTER_EXPLORE = "explore"
        const val FILTER_MY_TRIPS = "mytrips"
    }
}