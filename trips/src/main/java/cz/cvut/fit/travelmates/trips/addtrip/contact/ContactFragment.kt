package cz.cvut.fit.travelmates.trips.addtrip.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cz.cvut.fit.travelmates.trips.R
import cz.cvut.fit.travelmates.trips.databinding.FragmentContactBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactFragment : Fragment() {

    private lateinit var binding: FragmentContactBinding
    private val viewModel: ContactViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.eventShowTripCreated.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.contact_message_created),
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.eventNavigateMain.observe(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.navigation_add_trip, true)
        }
        viewModel.eventNavigateBack.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        viewModel.eventError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), getString(R.string.contact_error), Toast.LENGTH_SHORT)
                .show()
        }
    }
}