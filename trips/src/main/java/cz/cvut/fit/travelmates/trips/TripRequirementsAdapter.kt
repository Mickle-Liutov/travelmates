package cz.cvut.fit.travelmates.trips

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
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
            binding.apply {
                textItemTripRequirement.text = item.name
                val (typeface, alpha) = if (item.isFulfilled)
                    Typeface.NORMAL to ALPHA_FULFILLED
                else
                    Typeface.BOLD to ALPHA_UNFULFILLED
                textItemTripRequirement.setTypeface(textItemTripRequirement.typeface, typeface)
                textItemTripRequirement.alpha = alpha
                imageItemTripRequirementProvided.isVisible = item.isFulfilled
            }
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

    companion object {
        private const val ALPHA_FULFILLED = 0.7F
        private const val ALPHA_UNFULFILLED = 1F
    }
}