package cz.cvut.fit.travelmates.core.bindings

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun View.setIsVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}