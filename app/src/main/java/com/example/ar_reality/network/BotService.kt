package com.example.ar_reality.network

import com.example.ar_reality.model.bot_response.BotResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


const val BASE_URL = "https://random-data-api.com"


interface BotInterface {

    @GET("api/v2/users")
    fun getBotDetails(): Response<BotResponse>
}

var retroService = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()


object BotService{
    val botservice : BotInterface by lazy {
        retroService.create(BotInterface::class.java)
    }
}