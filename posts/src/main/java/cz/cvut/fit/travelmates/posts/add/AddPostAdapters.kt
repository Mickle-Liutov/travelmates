package cz.cvut.fit.travelmates.posts.add

import android.widget.TextView
import androidx.databinding.BindingAdapter
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.location.LocationFormatter
import cz.cvut.fit.travelmates.posts.R

@BindingAdapter("addPostLocation")
fun TextView.setAddPostLocation(location: Location?) {
    val locationText = location?.let {
        LocationFormatter(it, context).formatLocationName()
    } ?: context.getString(R.string.add_post_location_not_set)
    text = context.getString(R.string.add_post_location_format, locationText)
}