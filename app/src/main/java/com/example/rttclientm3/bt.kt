package com.example.rttclientm3

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Build
import android.os.Message
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID


var btIsReady by mutableStateOf(false)
lateinit var bluetoothManager: BluetoothManager
lateinit var bluetoothAdapter: BluetoothAdapter

lateinit var connectThread: ConnectThread

lateinit var esp32device: BluetoothDevice

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
            val device = esp32device //bluetoothAdapter.getRemoteDevice(mac)
            device.let {
                connectThread = ConnectThread(it)
                connectThread.start()
            }
        }
    }

    fun sendMessage(message: String){
        connectThread.rThread.sendMessage(message.toByteArray())
    }

}


class ConnectThread(private val device: BluetoothDevice) : Thread() {
    private val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    //00001101-0000-1000-8000-00805f9b34fb

    private var mSocket: BluetoothSocket? = null

    lateinit var rThread: ReceiveThread

    init {

        try {
            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        } catch (i: IOException) {


        }

    }

    override fun run() {

        try {
            Timber.i("Подключение...")
            mSocket?.connect()
            Timber.i("Подключились к устройсву")
            rThread = ReceiveThread(mSocket!!)
            rThread.start()
        } catch (i: IOException) {
            Timber.e("Не смогли подключиться к устройсву")
        }

    }

    fun closeConnection() {
        try {
            mSocket?.close()
        } catch (i: IOException) {


        }

    }


}

class ReceiveThread(private val bSocket: BluetoothSocket) : Thread() {

    var inStream: InputStream? = null
    var outStream: OutputStream? = null

    init {

        try {
            inStream = bSocket.inputStream
        } catch (i: IOException) {
        }

        try {
            outStream = bSocket.outputStream
        } catch (i: IOException) {
        }
    }

    override fun run() {
        //val buf = ByteArray(128)
        while (true) {
            try {
                val buf = inStream?.bufferedReader() //.read(buf)

                //val message = buf?.let { String(it, 0, buf.size) }

                if (buf != null) {
                    Timber.i(buf.readLine().toString())
                }
            } catch (i: IOException) {
                break  //При отключении подключения
            }
        }
    }

    fun sendMessage(byteArray : ByteArray)
    {
        try {
            outStream?.write(byteArray)
        }
        catch (i : IOException)
        {

        }
    }

}

