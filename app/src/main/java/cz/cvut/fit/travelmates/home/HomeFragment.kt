package cz.cvut.fit.travelmates.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fit.travelmates.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupTripsList()
        setupPostsList()
    }

    private fun setupObservers() {
        viewModel.eventNavigateAuth.observe(viewLifecycleOwner) {
            findNavController().navigate(HomeFragmentDirections.actionToAuth())
        }
        viewModel.eventNavigateTrips.observe(viewLifecycleOwner) {
            findNavController().navigate(HomeFragmentDirections.actionToExplore())
        }
        viewModel.eventNavigatePosts.observe(viewLifecycleOwner) {
            findNavController().navigate(HomeFragmentDirections.actionToPosts())
        }
        viewModel.eventNavigateOrganize.observe(viewLifecycleOwner) {
            findNavController().navigate(HomeFragmentDirections.actionHomeToAddTrip())
        }
        viewModel.eventNavigateTripDetails.observe(viewLifecycleOwner) { tripId ->
            findNavController().navigate(HomeFragmentDirections.actionHomeToDetails(tripId))
        }
    }

    private fun setupTripsList() {
        val tripsAdapter = HomeTripsAdapter().apply {
            onTripPressed = viewModel::onTripPressed
        }
        binding.recyclerHomeTrips.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = tripsAdapter
        }
        viewModel.trips.observe(viewLifecycleOwner) {
            tripsAdapter.submitList(it)
        }
    }

    private fun setupPostsList() {
        val postsAdapter = HomePostsAdapter()
        binding.recyclerHomePosts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = postsAdapter
        }
        viewModel.posts.observe(viewLifecycleOwner) {
            postsAdapter.submitList(it)
        }
    }
}