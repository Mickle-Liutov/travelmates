package cz.cvut.fit.travelmates.mytrips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cz.cvut.fit.travelmates.databinding.FragmentMyTripsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTripsFragment : Fragment() {

    private lateinit var binding: FragmentMyTripsBinding
    private val viewModel: MyTripsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyTripsBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        //TODO
    }
}