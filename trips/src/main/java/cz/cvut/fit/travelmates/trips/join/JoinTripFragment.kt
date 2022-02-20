package cz.cvut.fit.travelmates.trips.join

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fit.travelmates.trips.R
import cz.cvut.fit.travelmates.trips.databinding.FragmentJoinTripBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinTripFragment : Fragment() {

    private lateinit var binding: FragmentJoinTripBinding
    private val viewModel: JoinTripViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJoinTripBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupEquipmentList()
    }

    private fun setupObservers() {
        viewModel.eventRequestSent.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.join_trip_message_sent),
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.eventNavigateBack.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun setupEquipmentList() {
        val equipmentAdapter = ProvidedRequirementsAdapter().apply {
            onItemChecked = viewModel::onItemChecked
        }
        binding.recyclerJoinTripEquipment.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = equipmentAdapter
        }
        viewModel.equipment.observe(viewLifecycleOwner) {
            equipmentAdapter.submitList(it)
        }
    }
}