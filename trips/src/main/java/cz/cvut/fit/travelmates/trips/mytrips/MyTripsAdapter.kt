package cz.cvut.fit.travelmates.trips.mytrips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip
import cz.cvut.fit.travelmates.trips.TripRequirementsAdapter
import cz.cvut.fit.travelmates.trips.databinding.ItemMyTripBinding

class MyTripsAdapter : ListAdapter<Trip, MyTripsAdapter.TripViewHolder>(TripDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        return TripViewHolder(
            ItemMyTripBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind()
    }

    inner class TripViewHolder(private val binding: ItemMyTripBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val requirementsAdapter = TripRequirementsAdapter()

        init {
            binding.recyclerMyTripItemEquipment.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = requirementsAdapter
            }
        }

        fun bind() {
            val item = getItem(adapterPosition)
            binding.apply {
                textMyTripItemTitle.text = item.title
                textMyTripItemOwner.text = item.owner.name
                textMyTripItemDescription.text = item.description
                textMyTripItemState.text = item.state
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