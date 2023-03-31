package com.example.rttclientm3.network

import kotlinx.coroutines.channels.Channel

data class NetCommand(var cmd : String)


//Канал передачи
val channelNetworkIn = Channel<String>(1000000)

val channelCommand = Channel<NetCommand>(1000000)
val channelLastString = Channel<String>(1000000)


