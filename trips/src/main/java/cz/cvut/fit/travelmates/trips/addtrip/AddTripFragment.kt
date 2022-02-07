package cz.cvut.fit.travelmates.trips.addtrip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fit.travelmates.trips.addtrip.requirements.AddRequirementsAdapter
import cz.cvut.fit.travelmates.trips.databinding.FragmentAddTripBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTripFragment : Fragment() {

    private lateinit var binding: FragmentAddTripBinding
    private val viewModel: AddTripViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTripBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupRequirements()
    }

    private fun setupObservers() {
        viewModel.eventNavigateAddRequirement.observe(viewLifecycleOwner) {
            findNavController().navigate(AddTripFragmentDirections.actionToAddRequirement())
        }
        viewModel.eventShowTripCreated.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Trip was created", Toast.LENGTH_SHORT).show()
        }
        viewModel.eventNavigateBack.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(
            KEY_NEW_REQUIREMENT
        )?.observe(viewLifecycleOwner) { newRequirement ->
            viewModel.onRequirementAdded(newRequirement)
        }
    }

    private fun setupRequirements() {
        val requirementsAdapter = AddRequirementsAdapter().apply {
            onAddPressed = viewModel::onAddRequirementPressed
            onDeletePressed = viewModel::onDeleteRequirementPressed
        }
        binding.recyclerCreateTripRequirements.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = requirementsAdapter
        }
        viewModel.requirementsUi.observe(viewLifecycleOwner) {
            requirementsAdapter.submitList(it)
        }
    }

    companion object {
        const val KEY_NEW_REQUIREMENT = "requirement"
    }
}