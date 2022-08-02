package cz.cvut.fit.travelmates.home.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.travelmates.databinding.ItemHomeAddPostBinding
import cz.cvut.fit.travelmates.databinding.ItemHomePostBinding

/**
 * Adapter for displaying posts on home screen
 */
class HomePostsAdapter :
    ListAdapter<HomePostItem, HomePostsAdapter.HomePostItemViewHolder>(HomePostDiff) {

    var onAddPostPressed: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePostItemViewHolder {
        return when (viewType) {
            HomePostItem.TYPE_ITEM -> {
                val binding =
                    ItemHomePostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HomePostViewHolder(binding)
            }
            HomePostItem.TYPE_ADD -> {
                val binding =
                    ItemHomeAddPostBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                HomeAddPostViewHolder(binding)
            }
            else -> throw IllegalStateException("Unknown viewType: $viewType")
        }

    }

    override fun onBindViewHolder(holder: HomePostItemViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    abstract class HomePostItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind()
    }

    inner class HomePostViewHolder(private val binding: ItemHomePostBinding) :
        HomePostItemViewHolder(binding.root) {
        override fun bind() {
            val item = getItem(adapterPosition) as HomePost
            binding.item = item.post
        }
    }

    inner class HomeAddPostViewHolder(private val binding: ItemHomeAddPostBinding) :
        HomePostItemViewHolder(binding.root) {
        override fun bind() {
            binding.imageHomeAddPostAdd.setOnClickListener {
                onAddPostPressed?.invoke()
            }
        }
    }

    object HomePostDiff : DiffUtil.ItemCallback<HomePostItem>() {
        override fun areItemsTheSame(oldItem: HomePostItem, newItem: HomePostItem): Boolean {
            return oldItem.isSameAs(newItem)
        }

        override fun areContentsTheSame(oldItem: HomePostItem, newItem: HomePostItem): Boolean {
            return oldItem.hasSameContentAs(newItem)
        }
    }
}