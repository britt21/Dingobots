package com.example.ar_reality.helper

import retrofit2.Response

open class NetworkHelper<T>(var data: T? = null, var message: String? = null) {

    class Success<T>(data: T, message: String? = null) : NetworkHelper<T>(data, message)
    class Message<T>(message: String?, data: T? = null) : NetworkHelper<T>(data, message)
    class Loading<T> : NetworkHelper<T>()

}


class BaseClient {
    fun <T> handleResponse(response: Response<T>): NetworkHelper<T> {
        println("ETCASE: ${response.raw().request().url()}")
        when {
            response.isSuccessful -> {
                println("HELLOBODYBOB: ${response.body()}")
                var body = response.body()!!
                return NetworkHelper.Success(body)
            }

            response.code() == 404 || response.code() == 400 || response.code() == 401 || response.code() == 403 || response.code() == 500 || response.code() == 503 || response.code() == 422 -> {
                var message = response.errorBody()?.string()

                println("DSA: ${message}")
                println("HELLOMESSAGE: ${message}")

                return NetworkHelper.Message(message)
            }

            else -> {
                return NetworkHelper.Message("An Unexpected Error Occured")
            }
        }
    }

}