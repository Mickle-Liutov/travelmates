package cz.cvut.fit.travelmates.images

import android.graphics.Bitmap
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.net.URLDecoder

class ImagesRepository {

    private val storage = Firebase.storage

    /**
     * Upload image to remote storage
     * @return reference to uploaded image
     */
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun uploadImage(image: Bitmap, location: String): String {
        val stream = ByteArrayOutputStream()
        val ref = storage.getReference(location)

        image.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val data = stream.toByteArray()
        ref.putBytes(data).await()
        return URLDecoder.decode(ref.toString(), Charsets.UTF_8.name())
    }

}