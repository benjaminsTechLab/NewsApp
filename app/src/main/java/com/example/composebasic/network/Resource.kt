package com.example.composebasic.network

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Resource<out T>(
    val data: T? = null,
    val message: String = ""
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(message: String = "") : Resource<T>(message = message)
}
