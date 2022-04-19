package cz.cvut.fit.travelmates.auth.register

import android.util.Patterns
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
class RegisterViewModel @Inject constructor(
    private val formValidator: FormValidator,
    private val authRepository: AuthRepository,
    private val resourcesProvider: ResourcesProvider
) : ViewModel() {

    //Shows error that password don't match each other
    private val _eventPasswordMismatch = SingleLiveEvent<Unit>()
    val eventPasswordMismatch = _eventPasswordMismatch.immutable()

    //Shows message that user was registered
    private val _eventRegistered = SingleLiveEvent<Unit>()
    val eventRegistered = _eventRegistered.immutable()

    //Shows generic string error
    private val _eventError = SingleLiveEvent<String>()
    val eventError = _eventError.immutable()

    //Navigates back
    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    //Email of user, synchronized with input field
    val typedEmail = MutableStateFlow("")

    //Validation error of email
    private val _emailError = MutableLiveData<ValidationError?>()
    val emailErrorVisible = _emailError.immutable()

    //Name of user, synchronized with input field
    val typedName = MutableStateFlow("")

    //Password of user, synchronized with input field
    val typedPassword = MutableStateFlow("")

    //Validation error of password
    private val _passwordError = MutableLiveData<ValidationError?>()
    val passwordError = _passwordError.immutable()

    //Confirm password of user, synchronized with input field
    val typedConfirmPassword = MutableStateFlow("")

    //Whether continue button is active or not
    private val _isContinueActive = MutableStateFlow(false)
    val isContinueActive = _isContinueActive.asLiveData()

    //ViewState for register action
    private val viewState = MutableStateFlow(ViewState.CONTENT)
    val contentVisible = viewState.map { it == ViewState.CONTENT }.asLiveData()
    val loadingVisible = viewState.map { it == ViewState.LOADING }.asLiveData()

    init {
        setupValidations()
    }

    fun onRegisterPressed() {
        val email = typedEmail.value
        val name = typedName.value
        val password = typedPassword.value
        val confirmedPassword = typedConfirmPassword.value
        //Check that password match each other
        if (password != confirmedPassword) {
            _eventPasswordMismatch.call()
            return
        }
        viewModelScope.launchCatching(execute = {
            viewState.value = ViewState.LOADING
            authRepository.register(email, password, name)
            //Show message and navigate back is registration was successful
            _eventRegistered.call()
            _eventNavigateBack.call()
        }, catch = {
            _eventError.value = when (it) {
                is AuthException -> it.localizedMessage
                else -> resourcesProvider.getString(R.string.register_error_registration_failed)
            }
        }).invokeOnCompletion {
            viewState.value = ViewState.CONTENT
        }
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }

    /**
     * Setup validations of input fields
     */
    private fun setupValidations() {
        viewModelScope.launch {
            formValidator.combineValidations(
                //Regex pattern validation for email
                formValidator.getFieldValidation(
                    typedEmail,
                    { _emailError.value = it },
                    {
                        when {
                            !Patterns.EMAIL_ADDRESS.matcher(it)
                                .matches() -> ValidationError.INVALID_FORMAT
                            else -> null
                        }
                    }),
                //No validation for name
                formValidator.getFieldValidation(
                    typedName,
                    {},
                    { null }
                ),
                //Password validation for password
                formValidator.getFieldValidation(
                    typedPassword,
                    { _passwordError.value = it },
                    this@RegisterViewModel::passwordValidation
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