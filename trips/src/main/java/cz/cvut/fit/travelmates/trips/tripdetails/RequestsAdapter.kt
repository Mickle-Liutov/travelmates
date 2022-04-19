package cz.cvut.fit.travelmates.trips.tripdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.travelmates.core.bindings.setImageRef
import cz.cvut.fit.travelmates.mainapi.trips.models.Request
import cz.cvut.fit.travelmates.trips.R
import cz.cvut.fit.travelmates.trips.databinding.ItemRequestBinding

/**
 * Adapter for displaying incoming join requests on trip details screen
 */
class RequestsAdapter : ListAdapter<Request, RequestsAdapter.RequestViewHolder>(RequestDiff) {

    var onReviewPressed: ((Request) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding = ItemRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bind()
    }

    inner class RequestViewHolder(private val binding: ItemRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val item = getItem(adapterPosition)
            binding.apply {
                textItemRequestName.text = item.user.name
                imageItemRequest.setImageRef(
                    item.user.picture,
                    ContextCompat.getDrawable(root.context, R.drawable.ic_my_profile),
                    root.context.getString(R.string.transform_circle_crop)
                )
                buttonItemRequestReview.setOnClickListener {
                    onReviewPressed?.invoke(item)
                }
            }
        }
    }

    object RequestDiff : DiffUtil.ItemCallback<Request>() {
        override fun areItemsTheSame(oldItem: Request, newItem: Request): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Request, newItem: Request): Boolean {
            return oldItem == newItem
        }
    }
}