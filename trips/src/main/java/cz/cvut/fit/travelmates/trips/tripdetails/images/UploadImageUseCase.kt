package cz.cvut.fit.travelmates.trips.tripdetails.images

import android.graphics.Bitmap
import cz.cvut.fit.travelmates.images.ImagesRepository
import cz.cvut.fit.travelmates.trips.TripsRepository
import java.util.*

/**
 * Use case for uploading image for a trip
 */
class UploadImageUseCase(
    private val imagesRepository: ImagesRepository,
    private val tripsRepository: TripsRepository
) {

    /**
     * Upload the given bitmap for a trip with given id
     */
    suspend fun invoke(image: Bitmap, tripId: Long) {
        val imageLocation = TRIP_PICTURE_FORMAT.format("$tripId-${UUID.randomUUID()}")
        val imageRef = imagesRepository.uploadImage(image, imageLocation)
        tripsRepository.uploadTripImage(tripId, imageRef)
    }

    companion object {
        //Format for uploading trips images
        private const val TRIP_PICTURE_FORMAT = "tripsImages/%s"
    }

}