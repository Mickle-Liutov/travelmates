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

    //Shows error that password don't match each other
    private val _eventPasswordMismatch = SingleLiveEvent<Unit>()
    val eventPasswordMismatch = _eventPasswordMismatch.immutable()

    //Navigates to login screen
    private val _eventNavigateLogin = SingleLiveEvent<Unit>()
    val eventNavigateLogin = _eventNavigateLogin.immutable()

    //Shows message that reset was successful
    private val _eventShowResetSuccess = SingleLiveEvent<Unit>()
    val eventShowResetSuccess = _eventShowResetSuccess.immutable()

    //Shows generic string error
    private val _eventError = SingleLiveEvent<String>()
    val eventError = _eventError.immutable()

    //Navigates back
    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    //User's password, synchronized with input field
    val typedPassword = MutableStateFlow("")

    //Validation error of password
    private val _passwordError = MutableLiveData<ValidationError?>()
    val passwordError = _passwordError.immutable()

    //User's confirm password, synchronized with input field
    val typedConfirmPassword = MutableStateFlow("")

    //User's security code, synchronized with input field
    val typedSecurityCode = MutableStateFlow("")

    //Whether Continue button is active or not
    private val _isContinueActive = MutableStateFlow(false)
    val isContinueActive = _isContinueActive.asLiveData()

    //ViewState for recovery action
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
        //Check if entered passwords match each other
        if (password != confirmedPassword) {
            _eventPasswordMismatch.call()
            return
        }
        viewModelScope.launchCatching(execute = {
            viewState.value = ViewState.LOADING
            authRepository.confirmPasswordRecovery(password, code)
            //Show message that recovery was successful
            _eventShowResetSuccess.call()
            //Navigate back to login screen
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

    /**
     * Setup validations for input fields
     */
    private fun setupValidations() {
        viewModelScope.launch {
            //Combine validations for all fields
            formValidator.combineValidations(
                //No validation for security code
                formValidator.getFieldValidation(
                    typedSecurityCode,
                    {},
                    { null }
                ),
                //Password validation for password
                formValidator.getFieldValidation(
                    typedPassword,
                    { _passwordError.value = it },
                    this@ConfirmRecoveryViewModel::passwordValidation
                ),
                //No validation for confirm password
                formValidator.getFieldValidation(
                    typedConfirmPassword,
                    {},
                    { null }
                )
            ) { areAllValid ->
                //Continue button is active when all fields are valid
                _isContinueActive.value = areAllValid
            }
        }
    }

    /**
     * Validates a password
     */
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
        //Minimal required length of a password
        private const val PASSWORD_MIN_LENGTH = 8
    }

}