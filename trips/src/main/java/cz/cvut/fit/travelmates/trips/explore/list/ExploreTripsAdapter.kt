package cz.cvut.fit.travelmates.trips.explore.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.travelmates.mainapi.trips.models.Trip
import cz.cvut.fit.travelmates.mainapi.trips.models.TripDiff
import cz.cvut.fit.travelmates.trips.TripRequirementsAdapter
import cz.cvut.fit.travelmates.trips.databinding.ItemTripBinding
import java.time.format.DateTimeFormatter

class ExploreTripsAdapter : ListAdapter<Trip, ExploreTripsAdapter.ExploreTripViewHolder>(TripDiff) {

    var onTripPressed: ((Long) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreTripViewHolder {
        val binding =
            ItemTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExploreTripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExploreTripViewHolder, position: Int) {
        holder.bind()
    }

    inner class ExploreTripViewHolder(private val binding: ItemTripBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val requirementsAdapter = TripRequirementsAdapter()

        init {
            binding.recyclerTripItemEquipment.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = requirementsAdapter
            }
        }

        fun bind() {
            val item = getItem(adapterPosition)
            binding.apply {
                textTripItemTitle.text = item.title
                textTripItemDescription.text = item.description
                val date = DATE_FORMAT.format(item.suggestedDate)
                textTripsItemDateLocation.text = date
                root.setOnClickListener {
                    onTripPressed?.invoke(item.id)
                }
                //TODO Load pictures
            }
            requirementsAdapter.submitList(item.requirements.filter {
                !it.isFulfilled
            })
        }
    }

    companion object {
        private val DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d")
    }
}