package cz.cvut.fit.travelmates.posts.add

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.core.resources.ResourcesProvider
import cz.cvut.fit.travelmates.core.views.ViewState
import cz.cvut.fit.travelmates.images.ImagesRepository
import cz.cvut.fit.travelmates.location.Location
import cz.cvut.fit.travelmates.mainapi.posts.NewPost
import cz.cvut.fit.travelmates.posts.PostsRepository
import cz.cvut.fit.travelmates.posts.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    private val imagesRepository: ImagesRepository,
    private val resourcesProvider: ResourcesProvider
) : ViewModel() {

    private val _eventNavigateSetLocation = SingleLiveEvent<Unit>()
    val eventNavigateSetLocation = _eventNavigateSetLocation.immutable()

    private val _eventPickImage = SingleLiveEvent<Unit>()
    val eventPickImage = _eventPickImage.immutable()

    private val _eventError = SingleLiveEvent<String>()
    val eventError = _eventError.immutable()

    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    val typedDescription = MutableStateFlow("")

    private val _image = MutableStateFlow<Bitmap?>(null)
    val image = _image.filterNotNull().asLiveData()

    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asLiveData()

    private val viewState = MutableStateFlow(ViewState.CONTENT)

    val postVisible = combine(_image, _location, viewState) { image, location, state ->
        image != null && location != null && state == ViewState.CONTENT
    }.asLiveData()

    val loadingVisible = viewState.map {
        it == ViewState.LOADING
    }.asLiveData()

    fun onPostPressed() {
        val pickedImage = image.value ?: return
        val description = typedDescription.value
        val pickedLocation = _location.value ?: return
        viewModelScope.launchCatching(execute = {
            viewState.value = ViewState.LOADING
            val uploadedImage =
                imagesRepository.uploadImage(pickedImage, IMAGE_FORMAT.format(UUID.randomUUID()))
            postsRepository.createPost(NewPost(description, uploadedImage, pickedLocation))
            _eventNavigateBack.call()
        }, catch = {
            _eventError.value = resourcesProvider.getString(R.string.add_post_error_generic)
        }).invokeOnCompletion {
            viewState.value = ViewState.CONTENT
        }
    }

    fun onUploadImagePressed() {
        _eventPickImage.call()
    }

    fun onImagePicked(image: Bitmap) {
        _image.value = image
    }

    fun onSetLocationPressed() {
        _eventNavigateSetLocation.call()
    }

    fun onLocationPicked(location: Location) {
        this._location.value = location
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }

    companion object {
        private const val IMAGE_FORMAT = "posts/%s"
    }
}