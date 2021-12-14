package cz.cvut.fit.travelmates.auth.shared

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlin.coroutines.coroutineContext

@FlowPreview
class FormValidator {

    suspend fun getFieldValidation(
        inputFlow: Flow<String>,
        errorAction: (ValidationError?) -> Unit,
        validation: (String) -> ValidationError?
    ): Flow<Boolean> {
        inputFlow.debounce(VALIDATION_DEBOUNCE)
            .map {
                when {
                    it.isBlank() -> null
                    else -> validation.invoke(it)
                }
            }
            .onEach(errorAction)
            .launchIn(CoroutineScope(coroutineContext))

        return isFieldValid(inputFlow, validation)
    }

    suspend fun combineValidations(
        vararg flows: Flow<Boolean>,
        consumer: suspend (Boolean) -> Unit
    ) {
        combine(flows.toList()) { validations ->
            validations.all { it }
        }
            .onEach(consumer)
            .launchIn(CoroutineScope(coroutineContext))
    }

    private fun isFieldValid(
        inputFlow: Flow<String>,
        validation: (String) -> ValidationError?
    ): Flow<Boolean> {
        return inputFlow
            .map {
                validation.invoke(it) == null && it.isNotBlank()
            }
    }

    companion object {
        private const val VALIDATION_DEBOUNCE = 500L
    }

}