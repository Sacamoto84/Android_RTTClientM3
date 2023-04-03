package com.example.rttclientm3

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.SharedPreferences
import android.net.nsd.NsdServiceInfo
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.rttclientm3.network.UDP
import com.example.rttclientm3.network.bluetoothAdapter
import com.example.rttclientm3.network.bluetoothManager
import com.example.rttclientm3.network.bt
import com.example.rttclientm3.network.channelNetworkIn
import com.example.rttclientm3.network.decoder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class Initialization(private val context: Context) {

    // Declare NsdHelper object for service discovery
    private val nsdHelper: NsdHelper = object : NsdHelper(contex!!) {
        override fun onNsdServiceResolved(service: NsdServiceInfo) {
            // A new network service is available
            // Put your custom logic here!!!
        }

        override fun onNsdServiceLost(service: NsdServiceInfo) {
            // A network service is no longer available
            // Put your custom logic here!!!
        }
    }


    var isInitialised = false

    fun isInitialised(i : Boolean)
    {
        isInitialised = i
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun init0() {

        if (!isInitialised) {

            Timber.plant(Timber.DebugTree())
            Timber.i("Привет")

            bluetoothManager = context.getSystemService(BluetoothManager::class.java)
            bluetoothAdapter = bluetoothManager.adapter

            bt.getPairedDevices()
            bt.autoconnect()

            shared = context.getSharedPreferences("size", Context.MODE_PRIVATE)
            console_text.value = shared.getString("size", "12")?.toInt()?.sp ?: 12.sp

            //MARK: Вывод символа энтер
            isCheckedUseLiteralEnter.value = shared.getBoolean("enter", false)

            //MARK: Вывод номера строки
            isCheckedUselineVisible.value = shared.getBoolean("lineVisible", true)

            //Создаем список цветов из Json цветов
            colorJsonToList()

            // Initialize DNS-SD service discovery
            nsdHelper?.initializeNsd()

            // Start looking for available audio channels in the network
            nsdHelper?.discoverServices()

            val udp = UDP(8888, channelNetworkIn)
            GlobalScope.launch(
                Dispatchers.IO
            ) {
                udp.receiveScope()
            }


            decoder.run()
            decoder.addCmd("pong") {


            }


        }


    }
}