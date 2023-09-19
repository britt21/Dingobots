package com.example.ar_reality.ui

import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ar_reality.helper.BaseClient
import com.example.ar_reality.helper.NetworkHelper
import com.example.ar_reality.model.bot_response.BotResponse
import com.example.ar_reality.network.BotService
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    var baseClient = BaseClient()
   private val _livebotResponse =  MutableLiveData<NetworkHelper<BotResponse>>()
    val livebotResponse :  LiveData<NetworkHelper<BotResponse>> get() = _livebotResponse

    fun getBotDetail(){
        viewModelScope.launch {
            try {
                var response =BotService.botservice.getBotDetails()
                _livebotResponse.value = baseClient.handleResponse(response)

            }catch (e: Exception){
                println("FATALERROR: ${e.localizedMessage}")
                _livebotResponse.value = NetworkHelper.Message("${e.localizedMessage}")
            }
        }


    }
}