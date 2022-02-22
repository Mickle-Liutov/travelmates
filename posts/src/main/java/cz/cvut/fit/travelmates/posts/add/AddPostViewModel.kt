package cz.cvut.fit.travelmates.posts.add

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.images.ImagesRepository
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.posts.NewPost
import cz.cvut.fit.travelmates.posts.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    private val imagesRepository: ImagesRepository
) : ViewModel() {

    private val _eventNavigateSetLocation = SingleLiveEvent<Unit>()
    val eventNavigateSetLocation = _eventNavigateSetLocation.immutable()

    private val _eventPickImage = SingleLiveEvent<Unit>()
    val eventPickImage = _eventPickImage.immutable()

    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    val typedDescription = MutableStateFlow("")
    val image = MutableLiveData<Bitmap>()
    private val location = MutableLiveData<Location>()
    val locationText = location.map { "${it.lat};${it.lon}" }

    fun onPostPressed() {
        val pickedImage = image.value ?: return
        val description = typedDescription.value
        val pickedLocation = location.value ?: return
        //TODO Add loader
        viewModelScope.launchCatching(execute = {
            val uploadedImage =
                imagesRepository.uploadImage(pickedImage, IMAGE_FORMAT.format(UUID.randomUUID()))
            postsRepository.createPost(NewPost(description, uploadedImage, pickedLocation))
            _eventNavigateBack.call()
        }, catch = {
            Timber.e(it)
            //TODO
        })
    }

    fun onUploadImagePressed() {
        _eventPickImage.call()
    }

    fun onImagePicked(image: Bitmap) {
        this.image.value = image
    }

    fun onSetLocationPressed() {
        _eventNavigateSetLocation.call()
    }

    fun onLocationPicked(location: Location) {
        this.location.value = location
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }

    companion object {
        private const val IMAGE_FORMAT = "posts/%s"
    }
}