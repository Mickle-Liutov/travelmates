package cz.cvut.fit.travelmates.trips.request.reject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cz.cvut.fit.travelmates.trips.R
import cz.cvut.fit.travelmates.trips.databinding.DialogRejectBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RejectDialogFragment : DialogFragment() {

    private lateinit var binding: DialogRejectBinding
    private val viewModel: RejectDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogRejectBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.eventRejected.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.request_message_rejected),
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.eventPopReview.observe(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.navigation_request, true)
        }
        viewModel.eventError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), getString(R.string.reject_error), Toast.LENGTH_SHORT)
                .show()
        }
        viewModel.eventNavigateBack.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

}