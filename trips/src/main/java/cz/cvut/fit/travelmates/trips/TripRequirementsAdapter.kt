package cz.cvut.fit.travelmates.trips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.travelmates.mainapi.trips.models.TripRequirement
import cz.cvut.fit.travelmates.trips.databinding.ItemTripRequirementBinding

class TripRequirementsAdapter :
    ListAdapter<TripRequirement, TripRequirementsAdapter.TripRequirementViewHolder>(
        TripRequirementDiff
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripRequirementViewHolder {
        val binding =
            ItemTripRequirementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripRequirementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TripRequirementViewHolder, position: Int) {
        holder.bind()
    }

    inner class TripRequirementViewHolder(private val binding: ItemTripRequirementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val item = getItem(adapterPosition)
            binding.textItemTripRequirement.text = item.name
            //TODO Set is fulfilled state
        }
    }

    object TripRequirementDiff : DiffUtil.ItemCallback<TripRequirement>() {
        override fun areItemsTheSame(oldItem: TripRequirement, newItem: TripRequirement): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: TripRequirement,
            newItem: TripRequirement
        ): Boolean {
            return oldItem == newItem
        }
    }
}