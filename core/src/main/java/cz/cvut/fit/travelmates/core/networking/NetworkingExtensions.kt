package cz.cvut.fit.travelmates.core.networking

import retrofit2.HttpException
import retrofit2.Response

/**
 * Helper extension to extract result from Response
 */
fun <T> Response<T>.toBody(): T {
    val body = body()
    return when {
        !isSuccessful -> throw HttpException(this)
        body != null -> body
        else -> throw NoSuchElementException("Response has no body")
    }
}