package cz.cvut.fit.travelmates.mainapi.trips.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.cvut.fit.travelmates.location.Location
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class NewTripDto(
    @Json(name = "title")
    val title: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "location")
    val location: Location,
    @Json(name = "ownerContact")
    val ownerContact: String,
    @Json(name = "requirements")
    val requirements: List<Requirement>,
    @Json(name = "suggestedDate")
    val suggestedDate: LocalDate
)

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class Requirement(
    @Json(name = "name")
    val name: String
) : Parcelable