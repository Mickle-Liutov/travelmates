package cz.cvut.fit.travelmates.trips.mytrips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fit.travelmates.trips.databinding.FragmentMyTripsBinding
import cz.cvut.fit.travelmates.trips.mytrips.list.MyTripsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTripsFragment : Fragment() {

    private lateinit var binding: FragmentMyTripsBinding
    private val viewModel: MyTripsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyTripsBinding.inflate(inflater, container, false)
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
        viewModel.eventNavigateAdd.observe(viewLifecycleOwner) {
            findNavController().navigate(MyTripsFragmentDirections.actionToAddTrip())
        }
        viewModel.eventNavigateDetails.observe(viewLifecycleOwner) {
            findNavController().navigate(MyTripsFragmentDirections.actionToTripDetails(it))
        }
    }

    private fun setupList() {
        val tripsAdapter = MyTripsAdapter().apply {
            onTripPressed = viewModel::onTripPressed
        }
        binding.recyclerMyTrips.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tripsAdapter
        }
        viewModel.trips.observe(viewLifecycleOwner) {
            tripsAdapter.submitList(it)
        }
    }
}