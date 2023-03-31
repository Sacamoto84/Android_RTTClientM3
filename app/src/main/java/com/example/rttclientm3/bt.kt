package com.example.rttclientm3

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID


enum class BTstatus {
    DISCONNECT, CONNECTING, CONNECTED, RECEIVE
}

var btIsConnected by mutableStateOf(false)

var btIsReady by mutableStateOf(false)
lateinit var bluetoothManager: BluetoothManager
lateinit var bluetoothAdapter: BluetoothAdapter
lateinit var esp32device: BluetoothDevice

private var mSocket: BluetoothSocket? = null
private const val uuid = "00001101-0000-1000-8000-00805F9B34FB"

var btStatus = BTstatus.DISCONNECT

object bt {

    @SuppressLint("MissingPermission")
    fun getPairedDevices() {
        val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices

        pairedDevices.forEach {
            println(it.name)
            println(it.address)
            it.uuids.forEach { u ->
                println(u.toString())
            }
        }

        esp32device = pairedDevices.first { it.name == "ESP32" }

    }

    fun connect() {

        if (bluetoothAdapter.isEnabled) {
            btStatus = BTstatus.CONNECTING
            val device = esp32device //bluetoothAdapter.getRemoteDevice(mac)
            connectScope(device)
        }
    }

//    fun sendMessage(message: String) {
//        connectThread.rThread.sendMessage(message.toByteArray())
//    }

    @OptIn(DelicateCoroutinesApi::class)
    fun autoconnect() {
        GlobalScope.launch(Dispatchers.IO) {
            while(true) {
                delay(1000)
                if (btStatus == BTstatus.DISCONNECT) {
                    connect()
                }
            }
        }
    }


}


@OptIn(DelicateCoroutinesApi::class)
fun connectScope(device: BluetoothDevice) {

    GlobalScope.launch(Dispatchers.IO) {
        try {
            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
            Timber.i("Подключение...")
            mSocket?.connect()
            Timber.i("Подключились к устройству")
            btIsConnected = true
            btStatus = BTstatus.CONNECTED
            receiveScope()
        } catch (e: IOException) {
            btIsConnected = false
            mSocket?.close()
            Timber.e("Не смогли подключиться к устройсву ${e.message}")
            btStatus = BTstatus.DISCONNECT
        }
    }

}


@OptIn(DelicateCoroutinesApi::class)
fun receiveScope() {
    GlobalScope.launch(Dispatchers.IO) {
        btStatus = BTstatus.RECEIVE
        var inStream: InputStream? = null
        try {
            inStream = mSocket?.inputStream
        } catch (e: IOException) {
            Timber.e("Ошибка создания inputStream")
        }
        val buf = inStream?.bufferedReader(Charsets.UTF_8)
        while (true) {
            try {
                if (buf != null) {
                    val s = buf.read().toChar()
                    //Timber.i(s.toString())
                    channel.send(s.toString())
                }
            } catch (e: IOException) {
                Timber.e("Ошибка в приеменом потоке ${e.message}")
                withContext(Dispatchers.Main)
                {
                    btIsConnected = false
                    mSocket?.close()
                }
                break  //При отключении подключения
            }
        }
        btStatus = BTstatus.DISCONNECT
    }

}


//fun sendMessage(byteArray: ByteArray) {
//    try {
//        outStream?.write(byteArray)
//    } catch (i: IOException) {
//
//    }
//}


