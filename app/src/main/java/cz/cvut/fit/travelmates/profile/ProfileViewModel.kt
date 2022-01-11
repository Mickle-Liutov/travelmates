package cz.cvut.fit.travelmates.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.views.ViewState
import cz.cvut.fit.travelmates.mainapi.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val screenState = MutableStateFlow(ScreenState.SHOW)

    val isEditMode = screenState.map { it == ScreenState.EDIT }.asLiveData()

    private val user = MutableStateFlow<User?>(null)

    val typedName = MutableStateFlow("")
    val email = user.map { it?.email.orEmpty() }.asLiveData()

    private val loadUserViewState = MutableStateFlow(ViewState.LOADING)
    val loadUserLoadingVisible = loadUserViewState.map { it == ViewState.LOADING }.asLiveData()
    val loadUserContentVisible = loadUserViewState.map {
        it == ViewState.CONTENT
    }.asLiveData()

    //TODO Handle in UI
    val loadUserErrorVisible = loadUserViewState.map { it == ViewState.ERROR }.asLiveData()

    private val saveViewState = MutableStateFlow(ViewState.CONTENT)
    val saveLoadingVisible = saveViewState.map { it == ViewState.LOADING }.asLiveData()
    val saveButtonVisible = combine(screenState, saveViewState) { screenState, saveState ->
        screenState == ScreenState.EDIT && saveState == ViewState.CONTENT
    }.asLiveData()

    init {
        loadUser()
    }

    fun onRetryPressed() {
        loadUser()
    }

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
                //TODO Handle
                saveViewState.value = ViewState.CONTENT
                Timber.d(it)
            })
        }
    }

    private fun loadUser() {
        viewModelScope.launchCatching(execute = {
            loadUserViewState.value = ViewState.LOADING
            val user = userRepository.loadUser()
            this.user.value = user
            typedName.value = user.name
            loadUserViewState.value = ViewState.CONTENT
        }, catch = {
            //TODO Handle
            loadUserViewState.value = ViewState.ERROR
            Timber.d(it)
        })
    }

    enum class ScreenState {
        SHOW, EDIT
    }
}