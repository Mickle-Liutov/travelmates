package cz.cvut.fit.travelmates.trips.addtrip

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fit.travelmates.core.fragment.getNavigationResult
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.location.pick.PickLocationFragment
import cz.cvut.fit.travelmates.trips.R
import cz.cvut.fit.travelmates.trips.addtrip.requirements.AddRequirementsAdapter
import cz.cvut.fit.travelmates.trips.databinding.FragmentAddTripBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.LocalDate

@ExperimentalCoroutinesApi
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
        viewModel.eventNavigateContactStep.observe(viewLifecycleOwner) {
            findNavController().navigate(AddTripFragmentDirections.actionToContact(it))
        }
        viewModel.eventNavigatePickLocation.observe(viewLifecycleOwner) {
            findNavController().navigate(AddTripFragmentDirections.actionToPickLocation())
        }
        viewModel.eventNavigateBack.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        val backstackEntry = findNavController().getBackStackEntry(R.id.navigation_add_trip)
        backstackEntry.savedStateHandle.getLiveData<String?>(
            KEY_NEW_REQUIREMENT
        ).observe(viewLifecycleOwner) { newRequirement ->
            newRequirement?.let {
                viewModel.onRequirementAdded(it)
                backstackEntry.savedStateHandle.set(KEY_NEW_REQUIREMENT, null)
            }
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
        viewModel.eventPickDate.observe(viewLifecycleOwner) {
            //Show date picker
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    //Convert to LocalDate month ordering
                    val localDateMonth = month + 1
                    viewModel.onDateSet(LocalDate.of(year, localDateMonth, dayOfMonth))
                }, it.year, it.month.ordinal, it.dayOfMonth
            ).show()
        }
        getNavigationResult<Location>(PickLocationFragment.KEY_PICK_LOCATION)?.observe(
            viewLifecycleOwner
        ) {
            viewModel.onLocationPicked(it)
        }
    }

    companion object {
        const val KEY_NEW_REQUIREMENT = "requirement"
    }
}