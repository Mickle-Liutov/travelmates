package cz.cvut.fit.travelmates.core.bindings

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.Transformation
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import cz.cvut.fit.travelmates.core.R
import io.github.rosariopfernandes.firecoil.load
import kotlinx.coroutines.runBlocking

/**
 * Adapter for setting image reference to ImageView
 *
 * @param imageRef image's reference
 * @param placeholder placeholder to be shown while loading
 * @param imageTransform transform to apply to image
 */
@BindingAdapter(value = ["imageRef", "placeholder", "imageTransform"], requireAll = false)
fun ImageView.setImageRef(imageRef: String?, placeholder: Drawable?, imageTransform: String?) =
    runBlocking {
        val transformation: Transformation? = when (imageTransform) {
            context.getString(R.string.transform_circle_crop) -> CircleCropTransformation()
            else -> null
        }
        imageRef?.let {
            val ref = Firebase.storage.getReferenceFromUrl(imageRef)
            load(ref) {
                placeholder(placeholder)
                error(placeholder)
                transformation?.let {
                    transformations(it)
                }
            }
        }
        if (imageRef == null) {
            load(placeholder)
        }
    }

/**
 * Adapter for setting image bitmap to ImageView
 *
 * @param imageBitmap bitmap to set
 */
@BindingAdapter("imageBitmap")
fun ImageView.setImageRef(imageBitmap: Bitmap?) {
    imageBitmap?.let {
        load(it)
    }
}