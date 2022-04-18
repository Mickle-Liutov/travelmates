package cz.cvut.fit.travelmates.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.AuthException
import cz.cvut.fit.travelmates.auth.R
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.core.resources.ResourcesProvider
import cz.cvut.fit.travelmates.core.views.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val login: LoginUseCase,
    private val resourcesProvider: ResourcesProvider
) : ViewModel() {

    private val _eventNavigateMain = SingleLiveEvent<Unit>()
    val eventNavigateMain = _eventNavigateMain.immutable()

    private val _eventNavigateRecovery = SingleLiveEvent<Unit>()
    val eventNavigateRecovery = _eventNavigateRecovery.immutable()

    private val _eventError = SingleLiveEvent<String>()
    val eventError = _eventError.immutable()

    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    val typedEmail = MutableStateFlow("")
    val typedPassword = MutableStateFlow("")

    val isContinueActive = combine(typedEmail, typedPassword) { email, password ->
        email.isNotBlank() && password.isNotBlank()
    }.asLiveData()

    private val viewState = MutableStateFlow(ViewState.CONTENT)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()

    fun onLoginPressed() {
        val email = typedEmail.value
        val password = typedPassword.value
        viewModelScope.launchCatching(execute = {
            viewState.value = ViewState.LOADING
            login.invoke(email, password)
            _eventNavigateMain.call()
        }, catch = {
            when (it) {
                is AuthException -> _eventError.value = it.localizedMessage
                else -> _eventError.value =
                    resourcesProvider.getString(R.string.login_error_login_failed)
            }
        }).invokeOnCompletion {
            viewState.value = ViewState.CONTENT
        }
    }

    fun onPasswordRecoveryPressed() {
        _eventNavigateRecovery.call()
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }

}