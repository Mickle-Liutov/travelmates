package cz.cvut.fit.travelmates.mainapi.trips.models

import com.squareup.moshi.Json

enum class UserType {
    @Json(name = "GUEST")
    GUEST,

    @Json(name = "MEMBER")
    MEMBER,

    @Json(name = "OWNER")
    OWNER
}