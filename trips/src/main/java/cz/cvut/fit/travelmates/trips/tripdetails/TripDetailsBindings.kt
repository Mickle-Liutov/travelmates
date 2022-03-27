package cz.cvut.fit.travelmates.trips.tripdetails

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@BindingAdapter("tripDate")
fun TextView.setTripDate(date: LocalDate?) {
    val formatter = DateTimeFormatter.ofPattern("MMM d")
    date?.let {
        text = formatter.format(it).lowercase()
    }
}