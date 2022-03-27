package cz.cvut.fit.travelmates.trips.request

import cz.cvut.fit.travelmates.core.networking.toBody
import cz.cvut.fit.travelmates.mainapi.requests.RejectRequestBody
import cz.cvut.fit.travelmates.mainapi.requests.RequestsService

class RequestsRepository(private val requestsService: RequestsService) {

    suspend fun acceptRequest(requestId: Long) {
        requestsService.acceptRequest(requestId).toBody()
    }

    suspend fun rejectRequest(requestId: Long, reason: String, allowResend: Boolean) {
        requestsService.rejectRequest(requestId, RejectRequestBody(reason, allowResend)).toBody()
    }

}