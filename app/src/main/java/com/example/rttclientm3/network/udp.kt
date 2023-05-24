package com.example.rttclientm3.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket

class UDP(private val port: Int = 8888, private val channel: Channel<String>) {

    suspend fun receiveScope() = withContext(Dispatchers.IO) {
        println("Запуск UDP receiveScope")
        val buffer = ByteArray(1024 * 1024)
        val socket = DatagramSocket(port)
        socket.broadcast = true
        val packet = DatagramPacket(buffer, buffer.size)
        socket.receiveBufferSize = 1024 * 1024
        while (true) {
            socket.receive(packet)
            val string = String(packet.data.copyOfRange(0, packet.length))
            //println("!UDPRoutine! packet RAW=[$string")
            channel.send(string)
        }
    }

}