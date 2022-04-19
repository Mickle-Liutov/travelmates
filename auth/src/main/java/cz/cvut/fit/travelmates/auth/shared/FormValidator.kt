package cz.cvut.fit.travelmates.auth.shared

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlin.coroutines.coroutineContext

/**
 * Validates input fields
 *
 * @property debounce debounce for checking and propagating errors
 */
@FlowPreview
class FormValidator(private val debounce: Long = VALIDATION_DEBOUNCE) {

    /**
     * Get validation for a single field
     *
     * @param inputFlow flow of field to be validated
     * @param errorAction action to be called on error
     * @param validation validation to apply on field
     * @return flow of wether field is valid or not
     */
    suspend fun getFieldValidation(
        inputFlow: Flow<String>,
        errorAction: (ValidationError?) -> Unit,
        validation: (String) -> ValidationError?
    ): Flow<Boolean> {
        inputFlow.debounce(debounce)
            .map {
                when {
                    //Blank fields don't produce an error
                    it.isBlank() -> null
                    else -> validation.invoke(it)
                }
            }
            .onEach { errorAction.invoke(it) }
            .launchIn(CoroutineScope(coroutineContext))

        return isFieldValid(inputFlow, validation)
    }

    /**
     * Combines validations of multiple fields
     *
     * @param flows validation flows
     * @param consumer action to call with overall validation status
     */
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

    /**
     * Checks if field is valid and not blank
     *
     * @param inputFlow flow of input field to validate
     * @param validation validation to apply
     * @receiver
     * @return
     */
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
        //Default debounce for validation
        private const val VALIDATION_DEBOUNCE = 500L
    }

}