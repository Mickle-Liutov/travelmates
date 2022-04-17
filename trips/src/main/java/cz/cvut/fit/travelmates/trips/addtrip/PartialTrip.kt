package cz.cvut.fit.travelmates.trips.addtrip

import android.os.Parcelable
import androidx.annotation.Keep
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.trips.models.Requirement
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Keep
@Parcelize
data class PartialTrip(
    val title: String,
    val description: String,
    val location: Location,
    val requirements: List<Requirement>,
    val suggestedDate: LocalDate
) : Parcelable