package cz.cvut.fit.travelmates.location

import android.content.Context
import android.location.Geocoder

class LocationFormatter(private val location: Location, private val context: Context) {

    private val geoCoder = Geocoder(context)

    fun formatLocationName(): String {
        val locations = geoCoder.getFromLocation(location.lat, location.lon, 1)
        return locations.firstOrNull()?.countryName ?: context.getString(R.string.location_unknown)
    }

}