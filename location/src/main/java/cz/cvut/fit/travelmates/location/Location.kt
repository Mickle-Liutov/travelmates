package cz.cvut.fit.travelmates.location

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "lat")
    val lat: Double,
    @Json(name = "lon")
    val lon: Double
) : Parcelable