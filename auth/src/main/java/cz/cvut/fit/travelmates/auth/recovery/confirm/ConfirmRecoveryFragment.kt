package cz.cvut.fit.travelmates.auth.recovery.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cz.cvut.fit.travelmates.auth.R
import cz.cvut.fit.travelmates.auth.databinding.FragmentConfirmRecoveryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@FlowPreview
@AndroidEntryPoint
class ConfirmRecoveryFragment : Fragment() {

    private lateinit var binding: FragmentConfirmRecoveryBinding
    private val viewModel: ConfirmRecoveryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmRecoveryBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.eventPasswordMismatch.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.register_error_password_mismatch),
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.eventNavigateLogin.observe(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.navigation_start_recovery, true)
        }
        viewModel.eventShowResetSuccess.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.confirm_recovery_message_pass_reset),
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.eventNavigateBack.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }
}