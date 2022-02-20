package cz.cvut.fit.travelmates.trips.tripdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.travelmates.mainapi.trips.models.TripMember
import cz.cvut.fit.travelmates.trips.databinding.ItemTripMemberBinding

class MembersAdapter : ListAdapter<TripMember, MembersAdapter.MemberViewHolder>(TripMemberDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val binding =
            ItemTripMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind()
    }

    inner class MemberViewHolder(private val binding: ItemTripMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val item = getItem(adapterPosition)
            binding.apply {
                textItemTripMemberName.text = item.name
            }
        }
    }

    object TripMemberDiff : DiffUtil.ItemCallback<TripMember>() {
        override fun areItemsTheSame(oldItem: TripMember, newItem: TripMember): Boolean {
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: TripMember, newItem: TripMember): Boolean {
            return oldItem == newItem
        }
    }
}