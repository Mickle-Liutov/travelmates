package cz.cvut.fit.travelmates.mainapi.user

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class PublicUser(
    @Json(name = "name")
    val name: String,
    @Json(name = "picture")
    val picture: String?
) : Parcelable