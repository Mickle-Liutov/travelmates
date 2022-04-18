package cz.cvut.fit.travelmates.mainapi.trips.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UploadImageDto(
    @Json(name = "imageRef") val imageRef: String
)