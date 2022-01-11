package cz.cvut.fit.travelmates.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val screenState = MutableStateFlow(ScreenState.SHOW)

    val isEditMode = screenState.map { it == ScreenState.EDIT }.asLiveData()

    private val user = MutableStateFlow<User?>(null)

    val typedName = MutableStateFlow("")
    val email = user.map { it?.email.orEmpty() }.asLiveData()

    init {
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
        //TODO Save to server
        val oldUser = user.value
        oldUser?.let {
            val newUser = it.copy(name = typedName.value)
            user.value = newUser
            screenState.value = ScreenState.SHOW
        }
    }

    private fun loadUser() {
        //TODO Load
        val user = User("mail@mail.com", "John", null)
        this.user.value = user
        typedName.value = user.name
    }

    enum class ScreenState {
        SHOW, EDIT
    }
}