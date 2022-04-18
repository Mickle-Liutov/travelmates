package cz.cvut.fit.travelmates.trips.tripdetails.images

sealed class ImageUiItem {
    abstract fun isItemSame(other: ImageUiItem): Boolean
    abstract fun areContentsSame(other: ImageUiItem): Boolean
    abstract val viewType: Int

    companion object {
        const val TYPE_ITEM = 1
        const val TYPE_ADD = 2
    }
}

data class Image(val imageRef: String) : ImageUiItem() {
    override fun isItemSame(other: ImageUiItem) = this == other
    override fun areContentsSame(other: ImageUiItem) = this == other
    override val viewType = TYPE_ITEM
}

object AddImageItem : ImageUiItem() {
    override fun isItemSame(other: ImageUiItem) = this == other
    override fun areContentsSame(other: ImageUiItem) = this == other
    override val viewType = TYPE_ADD
}