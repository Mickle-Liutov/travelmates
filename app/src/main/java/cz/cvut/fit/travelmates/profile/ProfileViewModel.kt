package cz.cvut.fit.travelmates.profile

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.authapi.AuthRepository
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.core.views.ViewState
import cz.cvut.fit.travelmates.images.ImagesRepository
import cz.cvut.fit.travelmates.mainapi.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val imagesRepository: ImagesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    //Current state of the screen
    private val screenState = MutableStateFlow(ScreenState.SHOW)

    val isEditMode = screenState.map { it == ScreenState.EDIT }.asLiveData()

    private val user = MutableStateFlow<User?>(null)

    //Name of user, synchronized with input field
    val typedName = MutableStateFlow("")

    //Email of user
    val email = user.map { it?.email.orEmpty() }.asLiveData()

    //Profile image of user
    val userImage = user.map { it?.picture }.asLiveData()

    //ViewState for loading user's data
    private val loadUserViewState = MutableStateFlow(ViewState.LOADING)
    val loadUserLoadingVisible = loadUserViewState.map { it == ViewState.LOADING }.asLiveData()
    val loadUserContentVisible = loadUserViewState.map {
        it == ViewState.CONTENT
    }.asLiveData()
    val loadUserErrorVisible = loadUserViewState.map { it == ViewState.ERROR }.asLiveData()

    //ViewState for saving changes to user's data
    private val saveViewState = MutableStateFlow(ViewState.CONTENT)
    val saveLoadingVisible = saveViewState.map { it == ViewState.LOADING }.asLiveData()
    val saveButtonsVisible = combine(screenState, saveViewState) { screenState, saveState ->
        screenState == ScreenState.EDIT && saveState == ViewState.CONTENT
    }.asLiveData()

    //Whether edit button is visible or not
    val editVisible = combine(screenState, loadUserViewState) { screenState, loadState ->
        screenState == ScreenState.SHOW && loadState == ViewState.CONTENT
    }.asLiveData()

    //Shows error while saving changes
    private val _eventSaveError = SingleLiveEvent<Unit>()
    val eventSaveError = _eventSaveError.immutable()

    //Shows error while changing image
    private val _eventChangeImageError = SingleLiveEvent<Unit>()
    val eventChangeImageError = _eventChangeImageError.immutable()

    //Shows error while logging out
    private val _eventLogoutError = SingleLiveEvent<Unit>()
    val eventLogoutError = _eventLogoutError.immutable()

    //Navigates back
    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    //Shows image picker
    private val _eventPickImage = SingleLiveEvent<Unit>()
    val eventPickImage = _eventPickImage.immutable()

    //Navigates to main screen
    private val _eventNavigateMain = SingleLiveEvent<Unit>()
    val eventNavigateMain = _eventNavigateMain.immutable()

    init {
        loadUser()
    }

    fun onRetryPressed() = loadUser()

    fun onEditPressed() {
        screenState.value = ScreenState.EDIT
    }

    fun onCancelPressed() {
        typedName.value = user.value?.name.orEmpty()
        screenState.value = ScreenState.SHOW
    }

    fun onConfirmPressed() {
        val oldUser = user.value
        oldUser?.let {
            val newUser = it.copy(name = typedName.value)
            viewModelScope.launchCatching(execute = {
                saveViewState.value = ViewState.LOADING
                val updatedUser = userRepository.updateUser(newUser)
                user.value = updatedUser
                screenState.value = ScreenState.SHOW
                saveViewState.value = ViewState.CONTENT
            }, catch = {
                _eventSaveError.call()
                saveViewState.value = ViewState.CONTENT
            })
        }
    }

    fun onPickImagePressed() {
        _eventPickImage.call()
    }

    /**
     * Should be called when user selected profile picture
     */
    fun onProfileImagePicked(newImage: Bitmap) {
        val oldUser = user.value
        oldUser?.let {
            viewModelScope.launchCatching(execute = {
                val imageLocation = USER_PICTURE_FORMAT.format("${it.email}-${UUID.randomUUID()}")
                val imageRef = imagesRepository.uploadImage(newImage, imageLocation)
                val newUser = it.copy(picture = imageRef)
                val updatedUser = userRepository.updateUser(newUser)
                user.value = updatedUser
            }, catch = {
                _eventChangeImageError.call()
            })
        }
    }

    fun onLogoutPressed() {
        viewModelScope.launchCatching(execute = {
            authRepository.logout()
            _eventNavigateMain.call()
        }, catch = {
            _eventLogoutError.call()
        })
    }

    fun onBackPressed() {
        if (screenState.value == ScreenState.EDIT) {
            onCancelPressed()
            return
        }
        _eventNavigateBack.call()
    }

    /**
     * Loads user's profile
     */
    private fun loadUser() {
        viewModelScope.launchCatching(execute = {
            loadUserViewState.value = ViewState.LOADING
            val user = userRepository.loadUser()
            this.user.value = user
            typedName.value = user.name
            loadUserViewState.value = ViewState.CONTENT
        }, catch = {
            loadUserViewState.value = ViewState.ERROR
        })
    }

    /**
     * Represents possible states of Profile screen
     */
    enum class ScreenState {
        SHOW, EDIT
    }

    companion object {
        //Format for uploading profile images
        private const val USER_PICTURE_FORMAT = "profilePics/%s"
    }
}