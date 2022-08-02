package cz.cvut.fit.travelmates.auth.shared

/**
 * Represents error that happen when validating fields
 */
enum class ValidationError {
    TOO_SHORT, INVALID_FORMAT, MISSING_NUMBER, MISSING_LETTER, MISSING_BIG_LETTER
}