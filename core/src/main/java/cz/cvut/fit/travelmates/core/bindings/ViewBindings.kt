package cz.cvut.fit.travelmates.core.bindings

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun View.setIsVisible(visible: Boolean) {
    isVisible = visible
}

@BindingAdapter("visibleOrInvisible")
fun View.setIsVisibleOrInvisible(isVisible: Boolean) {
    isInvisible = !isVisible
}