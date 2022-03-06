package cz.cvut.fit.travelmates.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.travelmates.databinding.ItemHomeTripBinding
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip
import cz.cvut.fit.travelmates.mainapi.trips.models.TripDiff

class HomeTripsAdapter : ListAdapter<Trip, HomeTripsAdapter.HomeTripViewHolder>(TripDiff) {

    var onTripPressed: ((Trip) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTripViewHolder {
        val binding =
            ItemHomeTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeTripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeTripViewHolder, position: Int) {
        holder.bind()
    }

    inner class HomeTripViewHolder(private val binding: ItemHomeTripBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            val item = getItem(adapterPosition)
            binding.item = item
            binding.root.setOnClickListener {
                onTripPressed?.invoke(item)
            }
        }

    }
}