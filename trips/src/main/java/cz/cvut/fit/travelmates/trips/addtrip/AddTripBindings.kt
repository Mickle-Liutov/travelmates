package cz.cvut.fit.travelmates.trips.addtrip

import android.widget.TextView
import androidx.databinding.BindingAdapter
import cz.cvut.fit.travelmates.trips.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@BindingAdapter("suggestedDate")
fun TextView.setSuggestedDate(date: LocalDate?) {
    date?.let {
        val format = DateTimeFormatter.ofPattern("d MMMM, yyyy")
        text = context.getString(R.string.add_trip_suggested_date_format, format.format(it))
    }
}