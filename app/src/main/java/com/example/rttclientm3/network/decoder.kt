package com.example.rttclientm3.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext


class decoder(private val channelIn: Channel<String>, private val channelOutCommand: Channel<String>) {


    suspend fun decodeScope() = withContext(Dispatchers.IO) {


    }


}