package cz.cvut.fit.travelmates.mainapi.user

import retrofit2.Response
import retrofit2.http.POST

interface UserService {

    @POST("user/login")
    suspend fun loginUser(): Response<Unit>

}