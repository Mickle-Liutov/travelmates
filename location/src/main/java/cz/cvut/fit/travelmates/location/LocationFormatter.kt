package cz.cvut.fit.travelmates.location

import android.content.Context
import android.location.Geocoder

/**
 * Formatter for location's name
 */
class LocationFormatter(private val location: Location, private val context: Context) {

    //Geocoder allows to get address from GPS coordinates
    private val geoCoder = Geocoder(context)

    fun formatLocationName(): String {
        val locations = try {
            geoCoder.getFromLocation(location.lat, location.lon, 1)
        } catch (e: Exception) {
            emptyList()
        }
        //Return just country name as formatted location name
        return locations.firstOrNull()?.countryName ?: context.getString(R.string.location_unknown)
    }

}