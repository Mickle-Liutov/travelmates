package cz.cvut.fit.travelmates.mainapi.requests

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * API for requests
 */
interface RequestsService {

    @POST("requests/{$PATH_REQUEST_ID}/accept")
    suspend fun acceptRequest(@Path(PATH_REQUEST_ID) requestId: Long): Response<Unit>

    @POST("requests/{$PATH_REQUEST_ID}/reject")
    suspend fun rejectRequest(
        @Path(PATH_REQUEST_ID) requestId: Long,
        @Body body: RejectRequestBody
    ): Response<Unit>

    companion object {
        private const val PATH_REQUEST_ID = "requestId"
    }
}