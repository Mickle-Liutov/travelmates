package cz.cvut.fit.travelmates.location.pick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.cameraMoveEvents
import cz.cvut.fit.travelmates.core.fragment.setNavigationResult
import cz.cvut.fit.travelmates.location.databinding.FragmentPickLocationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PickLocationFragment : Fragment() {

    private lateinit var binding: FragmentPickLocationBinding
    private val viewModel: PickLocationViewModel by viewModels()

    private val mapFragment: SupportMapFragment by lazy {
        binding.mapPickLocation.getFragment() as SupportMapFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPickLocationBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMap()
        setupObservers()
    }

    private fun setupMap() {
        lifecycleScope.launch {
            val map = mapFragment.awaitMap()
            map.cameraMoveEvents().collect {
                viewModel.onCameraMoved(map.cameraPosition.target)
            }
        }
    }

    private fun setupObservers() {
        viewModel.eventSetResult.observe(viewLifecycleOwner) {
            setNavigationResult(it, KEY_PICK_LOCATION)
        }
        viewModel.eventNavigateBack.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    companion object {
        const val KEY_PICK_LOCATION = "pick_location"
    }
}