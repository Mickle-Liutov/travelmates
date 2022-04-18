package cz.cvut.fit.travelmates.trips.tripdetails.images

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.travelmates.core.bindings.setImageRef
import cz.cvut.fit.travelmates.trips.R
import cz.cvut.fit.travelmates.trips.databinding.ItemAddImageBinding
import cz.cvut.fit.travelmates.trips.databinding.ItemImageBinding

class ImagesAdapter : ListAdapter<ImageUiItem, ImagesAdapter.ImageUiViewHolder>(ImageDiff) {

    var onAddPressed: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageUiViewHolder {
        return when (viewType) {
            ImageUiItem.TYPE_ITEM -> ImageItemViewHolder(
                ItemImageBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            ImageUiItem.TYPE_ADD -> AddImageViewHolder(
                ItemAddImageBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            else -> throw IllegalStateException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: ImageUiViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    abstract class ImageUiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind()
    }

    inner class ImageItemViewHolder(private val binding: ItemImageBinding) :
        ImageUiViewHolder(binding.root) {
        override fun bind() {
            val item = getItem(adapterPosition) as Image
            binding.imageItemImage.apply {
                setImageRef(
                    item.imageRef,
                    ContextCompat.getDrawable(context, R.drawable.bg_placeholder),
                    null
                )
            }
        }
    }

    inner class AddImageViewHolder(private val binding: ItemAddImageBinding) :
        ImageUiViewHolder(binding.root) {
        override fun bind() {
            binding.buttonAddImage.setOnClickListener {
                onAddPressed?.invoke()
            }
        }
    }

    object ImageDiff : DiffUtil.ItemCallback<ImageUiItem>() {
        override fun areItemsTheSame(oldItem: ImageUiItem, newItem: ImageUiItem): Boolean {
            return oldItem.isItemSame(newItem)
        }

        override fun areContentsTheSame(oldItem: ImageUiItem, newItem: ImageUiItem): Boolean {
            return oldItem.areContentsSame(newItem)
        }
    }
}