package cz.cvut.fit.travelmates.trips.tripdetails

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import cz.cvut.fit.travelmates.trips.R
import cz.cvut.fit.travelmates.trips.TripRequirementsAdapter
import cz.cvut.fit.travelmates.trips.databinding.FragmentTripDetailsBinding
import cz.cvut.fit.travelmates.trips.tripdetails.images.ImagesAdapter
import cz.cvut.fit.travelmates.trips.tripdetails.members.MembersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTripDetailsBinding
    private val viewModel: TripDetailsViewModel by viewModels()

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }
            val fileUri = data?.data!!
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver,
                    fileUri
                )
            } else {
                val source = ImageDecoder.createSource(requireActivity().contentResolver, fileUri)
                ImageDecoder.decodeBitmap(source)
            }
            val softwareBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false)
            viewModel.onImagePicked(softwareBitmap)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripDetailsBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupMembers()
        setupEquipment()
        setupRequests()
        setupImages()
    }

    private fun setupObservers() {
        viewModel.eventNavigateJoin.observe(viewLifecycleOwner) {
            findNavController().navigate(TripDetailsFragmentDirections.actionToJoinTrip(it))
        }
        viewModel.eventNavigateRequest.observe(viewLifecycleOwner) {
            findNavController().navigate(TripDetailsFragmentDirections.actionToRequest(it))
        }
        viewModel.eventPickImage.observe(viewLifecycleOwner) {
            pickImage()
        }
        viewModel.eventNavigateMember.observe(viewLifecycleOwner) {
            findNavController().navigate(TripDetailsFragmentDirections.actionToTripMember(it))
        }
        viewModel.eventStopGatheringError.observe(viewLifecycleOwner) {
            showToast(R.string.trip_details_error_stop_gathering)
        }
        viewModel.eventFinishTripError.observe(viewLifecycleOwner) {
            showToast(R.string.trip_details_error_finish_trip)
        }
        viewModel.eventUploadImageError.observe(viewLifecycleOwner) {
            showToast(R.string.trip_details_error_upload_image)
        }
        viewModel.eventNavigateBack.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    private fun showToast(@StringRes messageRes: Int) {
        Toast.makeText(requireContext(), messageRes, Toast.LENGTH_SHORT).show()
    }

    private fun setupMembers() {
        val membersAdapter = MembersAdapter().apply {
            onMemberPressed = viewModel::onMemberPressed
        }
        binding.recyclerTripDetailsMembers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = membersAdapter
        }
        viewModel.members.observe(viewLifecycleOwner) {
            membersAdapter.submitList(it)
        }
    }

    private fun setupEquipment() {
        val equipmentAdapter = TripRequirementsAdapter()
        binding.recyclerTripDetailsEquipment.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = equipmentAdapter
        }
        viewModel.equipment.observe(viewLifecycleOwner) {
            equipmentAdapter.submitList(it)
        }
    }

    private fun setupRequests() {
        val requestsAdapter = RequestsAdapter().apply {
            onReviewPressed = viewModel::onReviewRequestPressed
        }
        binding.recyclerTripDetailsRequests.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = requestsAdapter
        }
        viewModel.requests.observe(viewLifecycleOwner) {
            requestsAdapter.submitList(it)
        }
    }

    private fun setupImages() {
        val imagesAdapter = ImagesAdapter().apply {
            onAddPressed = viewModel::onUploadImagePressed
        }
        binding.recyclerTripDetailsImages.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = imagesAdapter
        }
        viewModel.images.observe(viewLifecycleOwner) {
            imagesAdapter.submitList(it)
        }
    }

    private fun pickImage() {
        ImagePicker.with(this)
            .cropSquare()
            .createIntent { intent ->
                pickImageLauncher.launch(intent)
            }
    }
}