package cz.cvut.fit.travelmates.trips.addtrip.requirements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.travelmates.trips.databinding.ItemAddNewRequirementBinding
import cz.cvut.fit.travelmates.trips.databinding.ItemNewRequirementBinding

class AddRequirementsAdapter :
    ListAdapter<AddRequirementItem, AddRequirementsAdapter.AddRequirementsViewHolder>(
        AddRequirementItemDiff
    ) {

    var onAddPressed: (() -> Unit)? = null
    var onDeletePressed: ((RequirementItem) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddRequirementsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            AddRequirementItem.TYPE_ADD -> {
                AddRequirementViewHolder(
                    ItemAddNewRequirementBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            }
            AddRequirementItem.TYPE_ITEM -> {
                NewRequirementViewHolder(ItemNewRequirementBinding.inflate(inflater, parent, false))
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: AddRequirementsViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }

    abstract class AddRequirementsViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        abstract fun bind()
    }

    inner class NewRequirementViewHolder(private val binding: ItemNewRequirementBinding) :
        AddRequirementsViewHolder(binding.root) {
        override fun bind() {
            val item = getItem(adapterPosition) as RequirementItem
            binding.textItemNewRequirementName.text = item.name
            binding.buttonItemNewRequirementDelete.setOnClickListener {
                onDeletePressed?.invoke(item)
            }
        }
    }

    inner class AddRequirementViewHolder(private val binding: ItemAddNewRequirementBinding) :
        AddRequirementsViewHolder(binding.root) {
        override fun bind() {
            binding.buttonAddNewRequirement.setOnClickListener {
                onAddPressed?.invoke()
            }
        }
    }

    object AddRequirementItemDiff : DiffUtil.ItemCallback<AddRequirementItem>() {
        override fun areItemsTheSame(
            oldItem: AddRequirementItem,
            newItem: AddRequirementItem
        ): Boolean {
            return oldItem.isItemSame(newItem)
        }

        override fun areContentsTheSame(
            oldItem: AddRequirementItem,
            newItem: AddRequirementItem
        ): Boolean {
            return oldItem.areContentsSame(newItem)
        }
    }
}