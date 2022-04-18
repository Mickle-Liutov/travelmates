package cz.cvut.fit.travelmates.auth.recovery.confirm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.amplifyframework.auth.AuthException
import cz.cvut.fit.travelmates.auth.R
import cz.cvut.fit.travelmates.auth.shared.FormValidator
import cz.cvut.fit.travelmates.auth.shared.ValidationError
import cz.cvut.fit.travelmates.authapi.AuthRepository
import cz.cvut.fit.travelmates.core.coroutines.launchCatching
import cz.cvut.fit.travelmates.core.livedata.SingleLiveEvent
import cz.cvut.fit.travelmates.core.livedata.immutable
import cz.cvut.fit.travelmates.core.resources.ResourcesProvider
import cz.cvut.fit.travelmates.core.views.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class ConfirmRecoveryViewModel @Inject constructor(
    private val formValidator: FormValidator,
    private val authRepository: AuthRepository,
    private val resourcesProvider: ResourcesProvider
) : ViewModel() {

    private val _eventPasswordMismatch = SingleLiveEvent<Unit>()
    val eventPasswordMismatch = _eventPasswordMismatch.immutable()

    private val _eventNavigateLogin = SingleLiveEvent<Unit>()
    val eventNavigateLogin = _eventNavigateLogin.immutable()

    private val _eventShowResetSuccess = SingleLiveEvent<Unit>()
    val eventShowResetSuccess = _eventShowResetSuccess.immutable()

    private val _eventError = SingleLiveEvent<String>()
    val eventError = _eventError.immutable()

    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    val typedPassword = MutableStateFlow("")
    private val _passwordError = MutableLiveData<ValidationError?>()
    val passwordError = _passwordError.immutable()

    val typedConfirmPassword = MutableStateFlow("")
    val typedSecurityCode = MutableStateFlow("")

    private val _isContinueActive = MutableStateFlow(false)
    val isContinueActive = _isContinueActive.asLiveData()

    private val viewState = MutableStateFlow(ViewState.CONTENT)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()

    init {
        setupValidations()
    }

    fun onContinuePressed() {
        val password = typedPassword.value
        val confirmedPassword = typedConfirmPassword.value
        val code = typedSecurityCode.value
        if (password != confirmedPassword) {
            _eventPasswordMismatch.call()
            return
        }
        viewModelScope.launchCatching(execute = {
            viewState.value = ViewState.LOADING
            authRepository.confirmPasswordRecovery(password, code)
            _eventShowResetSuccess.call()
            _eventNavigateLogin.call()
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

    private fun setupValidations() {
        viewModelScope.launch {
            formValidator.combineValidations(
                formValidator.getFieldValidation(
                    typedSecurityCode,
                    {},
                    { null }
                ),
                formValidator.getFieldValidation(
                    typedPassword,
                    { _passwordError.value = it },
                    this@ConfirmRecoveryViewModel::passwordValidation
                ),
                formValidator.getFieldValidation(
                    typedConfirmPassword,
                    {},
                    { null }
                )
            ) { areAllValid ->
                _isContinueActive.value = areAllValid
            }
        }
    }

    private fun passwordValidation(password: String): ValidationError? {
        return when {
            password.length < PASSWORD_MIN_LENGTH -> ValidationError.TOO_SHORT
            !password.contains("[a-z]".toRegex()) -> ValidationError.MISSING_LETTER
            !password.contains("[A-Z]".toRegex()) -> ValidationError.MISSING_BIG_LETTER
            !password.contains("[0-9]".toRegex()) -> ValidationError.MISSING_NUMBER
            else -> null
        }
    }

    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
    }

}