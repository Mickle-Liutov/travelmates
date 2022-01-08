package cz.cvut.fit.travelmates.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.authapi.AuthRepository
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val idToken = MutableStateFlow("")

    private val _eventNavigateAuth = SingleLiveEvent<Unit>()
    val eventNavigateAuth = _eventNavigateAuth.immutable()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val hasSession = authRepository.hasValidSession()
            if (!hasSession) {
                _eventNavigateAuth.call()
                return@launch
            }
            val token = authRepository.getIdToken()
            idToken.value = token
        }
    }

}