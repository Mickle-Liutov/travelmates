package cz.cvut.fit.travelmates.location.preview

import cz.cvut.fit.travelmates.location.Location

/**
 * Helper class for composing url for location preview
 */
object PreviewUrlComposer {
    private const val ZOOM = 8
    private const val COLOR = "red"
    private const val SIZE = 380
    private const val SIZE_BIG = 640
    private const val MAPS_KEY = "AIzaSyDNKU3Vuu0vGNATolq45SK9dAgCV5vzIc8"
    private const val URL_FORMAT =
        "https://maps.googleapis.com/maps/api/staticmap?zoom=%d&size=%dx%d&markers=color:%s|%.6f,%.6f&key=%s"


    /**
     * Composes url of location preview image
     *
     * @param location location, for which preview should be created
     * @param form form of requested image
     * @return url of the preview
     */
    fun composeUrl(location: Location, form: PreviewForm): String {
        val horizontalSize = when (form) {
            PreviewForm.SQUARE -> SIZE
            PreviewForm.RECTANGLE -> SIZE_BIG
        }
        return URL_FORMAT.format(
            ZOOM, horizontalSize, SIZE, COLOR, location.lat, location.lon, MAPS_KEY
        )
    }

    enum class PreviewForm {
        SQUARE, RECTANGLE
    }

}