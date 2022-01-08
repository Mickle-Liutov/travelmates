package cz.cvut.fit.travelmates.auth.recovery.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fit.travelmates.authapi.AuthRepository
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StartRecoveryViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _eventNavigateConfirm = SingleLiveEvent<Unit>()
    val eventNavigateConfirm = _eventNavigateConfirm.immutable()

    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    val typedEmail = MutableStateFlow("")

    val isContinueActive = typedEmail.map {
        it.isNotBlank()
    }.asLiveData()

    fun onContinuePressed() {
        val email = typedEmail.value
        viewModelScope.launchCatching(execute = {
            authRepository.startPasswordRecovery(email)
            _eventNavigateConfirm.call()
        }, catch = {
            Timber.d("Start recovery failed")
        })
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }

}