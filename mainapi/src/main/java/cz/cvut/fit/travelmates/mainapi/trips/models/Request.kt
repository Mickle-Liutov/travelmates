package cz.cvut.fit.travelmates.mainapi.trips.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.cvut.fit.travelmates.mainapi.user.PublicUser
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class Request(
    @Json(name = "id")
    val id: Long,
    @Json(name = "user")
    val user: PublicUser,
    @Json(name = "providedEquipment")
    val providedEquipment: List<TripRequirement>,
    @Json(name = "contact")
    val contact: String,
    @Json(name = "message")
    val message: String,
    @Json(name = "state")
    val state: RequestState,
    @Json(name = "rejectionReason")
    val rejectionReason: String?
) : Parcelable

enum class RequestState {
    @Json(name = "PENDING")
    PENDING,

    @Json(name = "REJECTED_ALLOW_RESEND")
    REJECTED_ALLOW_RESEND,

    @Json(name = "REJECTED_NO_RESEND")
    REJECTED_NO_RESEND
}

