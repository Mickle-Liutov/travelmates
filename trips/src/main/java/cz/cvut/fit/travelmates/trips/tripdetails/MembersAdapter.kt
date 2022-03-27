package cz.cvut.fit.travelmates.trips.tripdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.travelmates.core.bindings.setImageRef
import cz.cvut.fit.travelmates.mainapi.trips.models.TripMember
import cz.cvut.fit.travelmates.trips.R
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
            val requirements = item.providedEquipment.joinToString(separator = ", ") { it.name }
            binding.apply {
                textItemTripMemberName.text = item.name
                textItemTripMemberRequirements.text = requirements
                imageItemTripMember.setImageRef(
                    item.picture,
                    ContextCompat.getDrawable(root.context, R.drawable.ic_my_profile),
                    root.context.getString(R.string.transform_circle_crop)
                )
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