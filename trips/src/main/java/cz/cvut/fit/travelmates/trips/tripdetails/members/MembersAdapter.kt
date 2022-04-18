package cz.cvut.fit.travelmates.trips.tripdetails.members

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fit.travelmates.core.bindings.setImageRef
import cz.cvut.fit.travelmates.trips.R
import cz.cvut.fit.travelmates.trips.databinding.ItemTripMemberBinding

class MembersAdapter :
    ListAdapter<TripParticipant, MembersAdapter.MemberViewHolder>(TripParticipantDiff) {

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
            val member = item.tripMember
            val requirements = member.providedEquipment.joinToString(separator = ", ") { it.name }
            binding.apply {
                textItemTripMemberName.text = member.name
                textItemTripMemberRequirements.text = requirements
                imageItemTripMemberOwner.isVisible = item.isOwner
                imageItemTripMember.setImageRef(
                    member.picture,
                    ContextCompat.getDrawable(root.context, R.drawable.ic_my_profile),
                    root.context.getString(R.string.transform_circle_crop)
                )
            }
        }
    }

    object TripParticipantDiff : DiffUtil.ItemCallback<TripParticipant>() {
        override fun areItemsTheSame(oldItem: TripParticipant, newItem: TripParticipant): Boolean {
            return oldItem.tripMember.email == newItem.tripMember.email
        }

        override fun areContentsTheSame(
            oldItem: TripParticipant,
            newItem: TripParticipant
        ): Boolean {
            return oldItem == newItem
        }
    }
}