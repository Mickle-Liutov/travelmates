package cz.cvut.fit.travelmates.trips.tripdetails.images

import android.graphics.Bitmap
import cz.cvut.fit.travelmates.images.ImagesRepository
import cz.cvut.fit.travelmates.trips.TripsRepository
import java.util.*

class UploadImageUseCase(
    private val imagesRepository: ImagesRepository,
    private val tripsRepository: TripsRepository
) {

    suspend fun invoke(image: Bitmap, tripId: Long) {
        val imageLocation = TRIP_PICTURE_FORMAT.format("$tripId-${UUID.randomUUID()}")
        val imageRef = imagesRepository.uploadImage(image, imageLocation)
        tripsRepository.uploadTripImage(tripId, imageRef)
    }

    companion object {
        private const val TRIP_PICTURE_FORMAT = "tripsImages/%s"
    }

}