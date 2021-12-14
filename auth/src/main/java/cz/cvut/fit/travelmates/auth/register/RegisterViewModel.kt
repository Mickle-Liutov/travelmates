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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val formValidator: FormValidator,
    private val authRepository: AuthRepository,
    private val resourcesProvider: ResourcesProvider
) : ViewModel() {

    private val _eventPasswordMismatch = SingleLiveEvent<Unit>()
    val eventPasswordMismatch = _eventPasswordMismatch.immutable()

    private val _eventRegistered = SingleLiveEvent<Unit>()
    val eventRegistered = _eventRegistered.immutable()

    private val _eventError = SingleLiveEvent<String>()
    val eventError = _eventError.immutable()

    private val _eventNavigateBack = SingleLiveEvent<Unit>()
    val eventNavigateBack = _eventNavigateBack.immutable()

    val typedEmail = MutableStateFlow("")
    private val _emailError = MutableLiveData<ValidationError?>()
    val emailErrorVisible = _emailError.immutable()

    val typedName = MutableStateFlow("")

    val typedPassword = MutableStateFlow("")
    private val _passwordError = MutableLiveData<ValidationError?>()
    val passwordError = _passwordError.immutable()

    val typedConfirmPassword = MutableStateFlow("")

    private val _isContinueActive = MutableStateFlow(false)
    val isContinueActive = _isContinueActive.asLiveData()

    init {
        setupValidations()
    }

    fun onRegisterPressed() {
        val email = typedEmail.value
        val name = typedName.value
        val password = typedPassword.value
        val confirmedPassword = typedConfirmPassword.value
        if (password != confirmedPassword) {
            _eventPasswordMismatch.call()
            return
        }
        viewModelScope.launchCatching(execute = {
            authRepository.register(email, password, name)
            _eventRegistered.call()
            _eventNavigateBack.call()
        }, catch = {
            _eventError.value = when (it) {
                is AuthException -> it.localizedMessage
                else -> resourcesProvider.getString(R.string.register_error_registration_failed)
            }
        })
    }

    fun onBackPressed() {
        _eventNavigateBack.call()
    }

    private fun setupValidations() {
        viewModelScope.launch {
            formValidator.combineValidations(
                formValidator.getFieldValidation(typedEmail,
                    { _emailError.value = it },
                    {
                        when {
                            !Patterns.EMAIL_ADDRESS.matcher(it)
                                .matches() -> ValidationError.INVALID_FORMAT
                            else -> null
                        }
                    }),
                formValidator.getFieldValidation(
                    typedName,
                    {},
                    { null }
                ),
                formValidator.getFieldValidation(
                    typedPassword,
                    { _passwordError.value = it },
                    this@RegisterViewModel::passwordValidation
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