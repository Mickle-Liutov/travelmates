package cz.cvut.fit.travelmates.trips.member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cz.cvut.fit.travelmates.trips.databinding.FragmentTripMemberBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripMemberFragment : Fragment() {

    private lateinit var binding: FragmentTripMemberBinding
    private val viewModel: TripMemberViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripMemberBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.eventNavigateBack.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }
}