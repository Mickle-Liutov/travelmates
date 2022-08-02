package cz.cvut.fit.travelmates.auth.recovery.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cz.cvut.fit.travelmates.auth.databinding.FragmentStartRecoveryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartRecoveryFragment : Fragment() {

    private lateinit var binding: FragmentStartRecoveryBinding
    private val viewModel: StartRecoveryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartRecoveryBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.eventNavigateConfirm.observe(viewLifecycleOwner) {
            findNavController().navigate(StartRecoveryFragmentDirections.actionToConfirmRecovery())
        }
        viewModel.eventError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        viewModel.eventNavigateBack.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }
}