package cz.cvut.fit.travelmates.mainapi.trips.models

import com.squareup.moshi.Json

enum class TripState {
    @Json(name = "GATHERING")
    GATHERING,

    @Json(name = "GATHERED")
    GATHERED,

    @Json(name = "FINISHED")
    FINISHED
}