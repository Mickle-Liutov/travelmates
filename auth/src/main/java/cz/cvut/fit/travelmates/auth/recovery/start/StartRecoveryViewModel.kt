package cz.cvut.fit.travelmates.auth.recovery.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.AuthException
import cz.cvut.fit.travelmates.auth.R
import cz.cvut.fit.travelmates.authapi.AuthRepository
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.core.resources.ResourcesProvider
import cz.cvut.fit.travelmates.core.views.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class StartRecoveryViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val resourcesProvider: ResourcesProvider
) : ViewModel() {

    private val _eventNavigateConfirm = SingleLiveEvent<Unit>()
    val eventNavigateConfirm = _eventNavigateConfirm.immutable()

    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    private val _eventError = SingleLiveEvent<String>()
    val eventError = _eventError.immutable()

    val typedEmail = MutableStateFlow("")

    val isContinueActive = typedEmail.map {
        it.isNotBlank()
    }.asLiveData()

    private val viewState = MutableStateFlow(ViewState.CONTENT)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()

    fun onContinuePressed() {
        val email = typedEmail.value
        viewModelScope.launchCatching(execute = {
            viewState.value = ViewState.LOADING
            authRepository.startPasswordRecovery(email)
            _eventNavigateConfirm.call()
        }, catch = {
            when (it) {
                is AuthException -> _eventError.value = it.localizedMessage
                else -> _eventError.value =
                    resourcesProvider.getString(R.string.confirm_recovery_error_recovery_failed)
            }
        }).invokeOnCompletion {
            viewState.value = ViewState.CONTENT
        }
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }

}