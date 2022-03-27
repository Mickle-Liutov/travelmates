package cz.cvut.fit.travelmates.location

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("tripLocation")
fun TextView.setTripLocation(location: Location?) {
    location?.let {
        val locationFormatter = LocationFormatter(it, context)
        text = locationFormatter.formatLocationName()
    }
}