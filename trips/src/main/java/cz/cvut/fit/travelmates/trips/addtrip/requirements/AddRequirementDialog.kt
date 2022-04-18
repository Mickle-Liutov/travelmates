package cz.cvut.fit.travelmates.trips.addtrip.requirements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cz.cvut.fit.travelmates.trips.addtrip.AddTripFragment
import cz.cvut.fit.travelmates.trips.databinding.DialogAddRequirementBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AddRequirementDialog : DialogFragment() {

    private lateinit var binding: DialogAddRequirementBinding
    private val viewModel: AddRequirementDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddRequirementBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.eventSetArgument.observe(viewLifecycleOwner) {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                AddTripFragment.KEY_NEW_REQUIREMENT,
                it
            )
        }
        viewModel.eventNavigateBack.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }
}