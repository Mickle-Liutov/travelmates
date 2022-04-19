package cz.cvut.fit.travelmates.authapi

import com.amazonaws.services.cognitoidentityprovider.model.UnauthorizedException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import timber.log.Timber

class AuthRepository {

    /**
     * Register a new user
     */
    suspend fun register(email: String, password: String, name: String) {
        val options =
            AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .userAttribute(AuthUserAttributeKey.name(), name)
                .build()
        val result = Amplify.Auth.signUp(email, password, options)
        if (!result.isSignUpComplete) {
            throw RuntimeException()
        }
    }

    /**
     * Login an existing user
     */
    suspend fun login(email: String, password: String) {
        val result = Amplify.Auth.signIn(email, password)
        if (!result.isSignInComplete) {
            throw RuntimeException()
        }
    }

    /**
     * Log user out
     */
    suspend fun logout() {
        Amplify.Auth.signOut()
    }

    /**
     * Start user's password recovery
     */
    suspend fun startPasswordRecovery(email: String) {
        val result = Amplify.Auth.resetPassword(email)
        if (!result.isPasswordReset) {
            Timber.d("Password not reset")
        }
    }

    /**
     * Confirm user's password recovery
     */
    suspend fun confirmPasswordRecovery(newPassword: String, securityCode: String) {
        Amplify.Auth.confirmResetPassword(newPassword, securityCode)
    }

    /**
     * Checks if user has a valid session
     *
     * @return whether user has a valida session
     */
    suspend fun hasValidSession(): Boolean {
        val session = Amplify.Auth.fetchAuthSession()
        return session.isSignedIn
    }

    /**
     * Gets id token of current user
     *
     * @return id token of user
     */
    suspend fun getIdToken(): String {
        val session = Amplify.Auth.fetchAuthSession() as AWSCognitoAuthSession
        return session.userPoolTokens.value?.idToken ?: throw UnauthorizedException("")
    }

}