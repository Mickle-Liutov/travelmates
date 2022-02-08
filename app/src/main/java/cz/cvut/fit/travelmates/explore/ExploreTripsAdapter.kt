package cz.cvut.fit.travelmates.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.travelmates.databinding.ItemExploreTripBinding
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip
import cz.cvut.fit.travelmates.trips.TripRequirementsAdapter

class ExploreTripsAdapter : ListAdapter<Trip, ExploreTripsAdapter.ExploreTripViewHolder>(TripDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreTripViewHolder {
        val binding =
            ItemExploreTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExploreTripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExploreTripViewHolder, position: Int) {
        holder.bind()
    }

    inner class ExploreTripViewHolder(private val binding: ItemExploreTripBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val requirementsAdapter = TripRequirementsAdapter()

        init {
            binding.recyclerExploreTripItemEquipment.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = requirementsAdapter
            }
        }

        fun bind() {
            val item = getItem(adapterPosition)
            binding.apply {
                textExploreTripItemTitle.text = item.title
                textExploreTripItemOwner.text = item.owner.name
                textExploreTripItemDescription.text = item.description
                //TODO Load pictures
            }
            requirementsAdapter.submitList(item.requirements)
        }
    }

    object TripDiff : DiffUtil.ItemCallback<Trip>() {
        override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem == newItem
        }
    }
}