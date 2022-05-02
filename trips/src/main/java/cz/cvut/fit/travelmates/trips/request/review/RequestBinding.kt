package cz.cvut.fit.travelmates.trips.request.review

import android.widget.TextView
import androidx.databinding.BindingAdapter
import cz.cvut.fit.travelmates.mainapi.trips.models.TripRequirement
import cz.cvut.fit.travelmates.trips.R

@BindingAdapter("providedEquipment")
fun TextView.setProvidedEquipment(requirements: List<TripRequirement>?) {
    val provided = requirements.orEmpty()
    text = if (provided.isEmpty()) {
        context.getString(R.string.request_requirements_empty)
    } else {
        context.getString(
            R.string.request_requirements_format,
            provided.joinToString(separator = "\n") { it.name })
    }
}