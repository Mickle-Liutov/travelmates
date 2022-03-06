package cz.cvut.fit.travelmates.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fit.travelmates.databinding.FragmentExploreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding
    private val viewModel: ExploreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupList()
    }

    private fun setupObservers() {
        viewModel.eventNavigateTripDetails.observe(viewLifecycleOwner) {
            findNavController().navigate(ExploreFragmentDirections.actionExploreToDetails(it))
        }
    }

    private fun setupList() {
        val tripsAdapter = ExploreTripsAdapter().apply {
            onTripPressed = viewModel::onTripPressed
        }
        binding.recyclerExploreTrips.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tripsAdapter
        }
        viewModel.exploreTrips.observe(viewLifecycleOwner) {
            tripsAdapter.submitList(it)
        }
    }
}