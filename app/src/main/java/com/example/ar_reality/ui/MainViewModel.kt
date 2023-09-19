package com.example.ar_reality.ui

import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ar_reality.helper.BaseClient
import com.example.ar_reality.helper.NetworkHelper
import com.example.ar_reality.model.bot_response.BotResponse
import com.example.ar_reality.network.BotService

class MainViewModel: ViewModel() {

    var baseClient = BaseClient()
    val _livebotResponse =  MutableLiveData<NetworkHelper<BotResponse>>()
    val liveBbotResponse :  LiveData<NetworkHelper<BotResponse>> get() = _livebotResponse

    fun getBotDetail(){
        var response =BotService.botservice.getBotDetails()
        _livebotResponse.value = baseClient.handleResponse(response)


    }
}