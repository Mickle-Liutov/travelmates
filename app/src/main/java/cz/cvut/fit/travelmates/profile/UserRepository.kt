package cz.cvut.fit.travelmates.profile

import cz.cvut.fit.travelmates.core.networking.toBody
import cz.cvut.fit.travelmates.mainapi.user.User
import cz.cvut.fit.travelmates.mainapi.user.UserService

class UserRepository(
    private val userService: UserService
) {

    suspend fun loadUser(): User {
        return userService.getUser().toBody()
    }

    suspend fun updateUser(newUser: User): User {
        return userService.updateUser(newUser).toBody()
    }

}