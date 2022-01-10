package cz.cvut.fit.travelmates.mainapi

import cz.cvut.fit.travelmates.authapi.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val authRepository: AuthRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        if (authRepository.hasValidSession()) {
            val authHeader = authRepository.getIdToken()
            val newRequest = chain.request().newBuilder()
                .addHeader(
                    AUTH_HEADER, BEARER_FORMAT.format(authHeader)
                )
                .build()
            return@runBlocking chain.proceed(newRequest)
        }
        return@runBlocking chain.proceed(chain.request())
    }

    companion object {
        private const val AUTH_HEADER = "Authorization"
        private const val BEARER_FORMAT = "Bearer %s"
    }
}