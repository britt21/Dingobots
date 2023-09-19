package com.example.ar_reality.model.bot_response

data class Subscription(
    val payment_method: String,
    val plan: String,
    val status: String,
    val term: String
)