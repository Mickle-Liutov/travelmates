package cz.cvut.fit.travelmates.auth.login

import cz.cvut.fit.travelmates.authapi.AuthRepository
import cz.cvut.fit.travelmates.core.networking.toBody
import cz.cvut.fit.travelmates.mainapi.user.UserService

/**
 * Use case for logging user in
 */
class LoginUseCase(
    private val authRepository: AuthRepository,
    private val userService: UserService
) {

    suspend fun invoke(email: String, password: String) {
        authRepository.login(email, password)
        try {
            userService.loginUser().toBody()
        } catch (e: Exception) {
            //Removing amplify session that was created
            authRepository.logout()
            throw e
        }
    }

}