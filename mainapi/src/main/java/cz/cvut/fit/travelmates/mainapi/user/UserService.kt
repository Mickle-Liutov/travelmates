package cz.cvut.fit.travelmates.mainapi.user

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

/**
 * API for users
 */
interface UserService {

    @POST("$PATH_USERS/login")
    suspend fun loginUser(): Response<Unit>

    @GET(PATH_USERS)
    suspend fun getUser(): Response<User>

    @PUT(PATH_USERS)
    suspend fun updateUser(@Body newUser: User): Response<User>

    companion object {
        private const val PATH_USERS = "users"
    }

}