package cz.cvut.fit.travelmates.trips.join

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.travelmates.trips.databinding.ItemProvidedRequirementBinding

class ProvidedRequirementsAdapter :
    ListAdapter<ProvidedRequirement, ProvidedRequirementsAdapter.ProvidedRequirementViewHolder>(
        ProvidedRequirementDiff
    ) {

    var onItemChecked: ((Long, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProvidedRequirementViewHolder {
        val binding = ItemProvidedRequirementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProvidedRequirementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProvidedRequirementViewHolder, position: Int) {
        holder.bind()
    }

    override fun onBindViewHolder(
        holder: ProvidedRequirementViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.firstOrNull() != null) {
            //Only checked state changed
            return
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    inner class ProvidedRequirementViewHolder(private val binding: ItemProvidedRequirementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val item = getItem(adapterPosition)
            binding.apply {
                textProvidedEquipmentName.text = item.name
                checkboxProvidedEquipment.isChecked = item.isChecked
                checkboxProvidedEquipment.setOnCheckedChangeListener { _, isChecked ->
                    onItemChecked?.invoke(item.id, isChecked)
                }
            }
        }
    }

    object ProvidedRequirementDiff : DiffUtil.ItemCallback<ProvidedRequirement>() {
        override fun areItemsTheSame(
            oldItem: ProvidedRequirement,
            newItem: ProvidedRequirement
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProvidedRequirement,
            newItem: ProvidedRequirement
        ): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(
            oldItem: ProvidedRequirement,
            newItem: ProvidedRequirement
        ): Any {
            return oldItem.isChecked to newItem.isChecked
        }
    }

}