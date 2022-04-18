package cz.cvut.fit.travelmates.trips.mytrips.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.travelmates.location.setLocationPreview
import cz.cvut.fit.travelmates.trips.TripRequirementsAdapter
import cz.cvut.fit.travelmates.trips.databinding.ItemMyTripSubtitleBinding
import cz.cvut.fit.travelmates.trips.databinding.ItemTripBinding
import java.time.format.DateTimeFormatter

class MyTripsAdapter :
    ListAdapter<MyTripsItem, MyTripsAdapter.MyTripItemViewHolder>(MyTripItemDiff) {

    var onTripPressed: ((MyTrip) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTripItemViewHolder {
        return when (viewType) {
            MyTripsItem.TYPE_ITEM -> TripViewHolder(
                ItemTripBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            MyTripsItem.TYPE_SUBTITLE -> SubtitleViewHolder(
                ItemMyTripSubtitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalStateException("Unknown viewtype: $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun onBindViewHolder(holder: MyTripItemViewHolder, position: Int) {
        holder.bind()
    }

    abstract class MyTripItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind()
    }

    inner class TripViewHolder(private val binding: ItemTripBinding) :
        MyTripItemViewHolder(binding.root) {

        private val requirementsAdapter = TripRequirementsAdapter()

        init {
            binding.recyclerTripItemEquipment.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = requirementsAdapter
            }
        }

        override fun bind() {
            val item = getItem(adapterPosition) as MyTrip
            binding.apply {
                textTripItemTitle.text = item.title
                textTripItemDescription.text = item.description
                val date = DATE_FORMAT.format(item.suggestedTime)
                textTripsItemDateLocation.text = date
                root.setOnClickListener {
                    onTripPressed?.invoke(item)
                }
                imageTripItem.setLocationPreview(item.location)
            }
            requirementsAdapter.submitList(item.requirements.filter {
                !it.isFulfilled
            })
        }
    }

    inner class SubtitleViewHolder(private val binding: ItemMyTripSubtitleBinding) :
        MyTripItemViewHolder(binding.root) {
        override fun bind() {
            val item = getItem(adapterPosition) as MyTripsSubtitle
            val formatter = MyTripsSubtitleFormatter(item, binding.root.context)
            binding.formatter = formatter
        }
    }

    object MyTripItemDiff : DiffUtil.ItemCallback<MyTripsItem>() {
        override fun areItemsTheSame(oldItem: MyTripsItem, newItem: MyTripsItem): Boolean {
            return oldItem.areItemsSame(newItem)
        }

        override fun areContentsTheSame(oldItem: MyTripsItem, newItem: MyTripsItem): Boolean {
            return oldItem.areContentsSame(newItem)
        }
    }

    companion object {
        private val DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d")
    }
}