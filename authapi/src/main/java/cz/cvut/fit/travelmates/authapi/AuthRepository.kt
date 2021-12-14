package cz.cvut.fit.travelmates.authapi

import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify

class AuthRepository {

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

}