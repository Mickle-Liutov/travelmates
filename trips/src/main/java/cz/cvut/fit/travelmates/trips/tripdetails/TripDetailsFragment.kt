package cz.cvut.fit.travelmates.trips.tripdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fit.travelmates.trips.TripRequirementsAdapter
import cz.cvut.fit.travelmates.trips.databinding.FragmentTripDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTripDetailsBinding
    private val viewModel: TripDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripDetailsBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupMembers()
        setupEquipment()
        setupRequests()
    }

    private fun setupObservers() {
        viewModel.eventNavigateJoin.observe(viewLifecycleOwner) {
            findNavController().navigate(TripDetailsFragmentDirections.actionToJoinTrip(it))
        }
        viewModel.eventNavigateRequest.observe(viewLifecycleOwner) {
            findNavController().navigate(TripDetailsFragmentDirections.actionToRequest(it))
        }
        viewModel.eventNavigateBack.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun setupMembers() {
        val membersAdapter = MembersAdapter()
        binding.recyclerTripDetailsMembers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = membersAdapter
        }
        viewModel.members.observe(viewLifecycleOwner) {
            membersAdapter.submitList(it)
        }
    }

    private fun setupEquipment() {
        val equipmentAdapter = TripRequirementsAdapter()
        binding.recyclerTripDetailsEquipment.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = equipmentAdapter
        }
        viewModel.equipment.observe(viewLifecycleOwner) {
            equipmentAdapter.submitList(it)
        }
    }

    private fun setupRequests() {
        val requestsAdapter = RequestsAdapter().apply {
            onReviewPressed = viewModel::onReviewRequestPressed
        }
        binding.recyclerTripDetailsRequests.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = requestsAdapter
        }
        viewModel.requests.observe(viewLifecycleOwner) {
            requestsAdapter.submitList(it)
        }
    }
}