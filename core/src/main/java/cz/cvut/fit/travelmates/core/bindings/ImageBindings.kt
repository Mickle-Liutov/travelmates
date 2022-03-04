package cz.cvut.fit.travelmates.core.bindings

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import io.github.rosariopfernandes.firecoil.load
import kotlinx.coroutines.runBlocking

@BindingAdapter(value = ["imageRef", "placeholder"], requireAll = false)
fun ImageView.setImageRef(imageRef: String?, placeholder: Drawable?) = runBlocking {
    imageRef?.let {
        val ref = Firebase.storage.getReferenceFromUrl(imageRef)
        load(ref) {
            placeholder(placeholder)
        }
    }
}

@BindingAdapter("imageBitmap")
fun ImageView.setImageRef(imageBitmap: Bitmap?) {
    imageBitmap?.let {
        load(it)
    }
}