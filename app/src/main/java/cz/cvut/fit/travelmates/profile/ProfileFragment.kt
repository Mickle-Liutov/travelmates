package cz.cvut.fit.travelmates.profile

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
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import cz.cvut.fit.travelmates.R
import cz.cvut.fit.travelmates.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode != Activity.RESULT_OK) {
                //Getting image didn't succeed
                return@registerForActivityResult
            }
            //Safe to assert not-null since result is RESULT_OK
            val fileUri = data?.data!!
            //Getting bitmap based on SDK version
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
            //Convert to software bitmap to avoid problems with hardware bitmaps
            val softwareBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false)
            viewModel.onProfileImagePicked(softwareBitmap)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        //Setting custom hardware back button behaviour
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onBackPressed()
        }
    }

    private fun setupObservers() {
        viewModel.eventSaveError.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.profile_error_save_failed),
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.eventChangeImageError.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.profile_error_image_upload_failed),
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.eventLogoutError.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.profile_error_logout_failed),
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.eventNavigateBack.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        viewModel.eventPickImage.observe(viewLifecycleOwner) {
            pickImage()
        }
        viewModel.eventNavigateMain.observe(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.navigation_home, false)
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