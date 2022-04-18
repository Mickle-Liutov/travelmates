package cz.cvut.fit.travelmates.location

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import cz.cvut.fit.travelmates.location.preview.PreviewUrlComposer

@BindingAdapter("tripLocation")
fun TextView.setTripLocation(location: Location?) {
    location?.let {
        val locationFormatter = LocationFormatter(it, context)
        text = locationFormatter.formatLocationName()
    }
}

@BindingAdapter(value = ["locationPreview", "previewForm"], requireAll = false)
fun ImageView.setLocationPreview(location: Location?, previewForm: String? = null) {
    location?.let {
        val imageForm = when (previewForm) {
            context.getString(R.string.location_preview_square) -> PreviewUrlComposer.PreviewForm.SQUARE
            context.getString(R.string.location_preview_rectangle) -> PreviewUrlComposer.PreviewForm.RECTANGLE
            else -> PreviewUrlComposer.PreviewForm.SQUARE
        }
        load(PreviewUrlComposer.composeUrl(it, imageForm))
    }
}