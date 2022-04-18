package cz.cvut.fit.travelmates.posts.add

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import cz.cvut.fit.travelmates.core.fragment.getNavigationResult
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.location.pick.PickLocationFragment
import cz.cvut.fit.travelmates.posts.databinding.FragmentAddPostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AddPostFragment : Fragment() {

    private lateinit var binding: FragmentAddPostBinding
    private val viewModel: AddPostViewModel by viewModels()

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
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.eventPickImage.observe(viewLifecycleOwner) {
            pickImage()
        }
        viewModel.eventNavigateSetLocation.observe(viewLifecycleOwner) {
            findNavController().navigate(AddPostFragmentDirections.actionPostToPickLocation())
        }
        getNavigationResult<Location>(PickLocationFragment.KEY_PICK_LOCATION)?.observe(
            viewLifecycleOwner
        ) {
            viewModel.onLocationPicked(it)
        }
        viewModel.eventError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        viewModel.eventNavigateBack.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
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