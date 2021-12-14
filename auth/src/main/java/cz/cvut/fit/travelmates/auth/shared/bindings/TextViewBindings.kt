package cz.cvut.fit.travelmates.auth.shared.bindings

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import cz.cvut.fit.travelmates.auth.R
import cz.cvut.fit.travelmates.auth.shared.ValidationError

@BindingAdapter("emailError")
fun setEmailError(textInputLayout: TextInputLayout, error: ValidationError?) {
    val errorText = when (error) {
        ValidationError.INVALID_FORMAT -> R.string.register_error_invalid_email
        else -> null
    }?.let { textInputLayout.context.getString(it) }
    textInputLayout.error = errorText
}

@BindingAdapter("passwordError")
fun setPasswordError(textInputLayout: TextInputLayout, error: ValidationError?) {
    val errorText = when (error) {
        ValidationError.TOO_SHORT -> R.string.register_error_invalid_password_short
        ValidationError.MISSING_NUMBER -> R.string.register_error_invalid_password_number
        ValidationError.MISSING_LETTER -> R.string.register_error_invalid_password_letter
        ValidationError.MISSING_BIG_LETTER -> R.string.register_error_invalid_password_big_letter
        else -> null
    }?.let { textInputLayout.context.getString(it) }
    textInputLayout.error = errorText
}
