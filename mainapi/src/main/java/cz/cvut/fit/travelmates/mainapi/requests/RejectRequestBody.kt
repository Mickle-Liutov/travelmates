package cz.cvut.fit.travelmates.mainapi.requests

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO for rejecting join request
 */
@JsonClass(generateAdapter = true)
data class RejectRequestBody(
    @Json(name = "reason")
    val reason: String,
    @Json(name = "allowResend")
    val allowResend: Boolean
)