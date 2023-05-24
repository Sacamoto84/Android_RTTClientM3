package com.example.rttclientm3

import android.content.Context
import android.net.nsd.NsdServiceInfo
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.rttclientm3.network.BT
import com.example.rttclientm3.network.UDP
import com.example.rttclientm3.network.channelNetworkIn
import com.example.rttclientm3.network.decoder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import libs.console.LineTextAndColor
import libs.console.PairTextAndColor
import libs.lan.readLocalIP
import timber.log.Timber


class Initialization(private val context: Context) {

    private var isInitialised = false

    private var nsdHelper: NsdHelper? = null

    init {

        // Declare NsdHelper object for service discovery
        nsdHelper = object : NsdHelper(context) {
            override fun onNsdServiceResolved(service: NsdServiceInfo) {
                // A new network service is available
                // Put your custom logic here!!!
            }

            override fun onNsdServiceLost(service: NsdServiceInfo) {
                // A network service is no longer available
                // Put your custom logic here!!!
            }
        }

    }


    @OptIn(DelicateCoroutinesApi::class)
    fun init0() {

        if (!isInitialised) {

            Timber.plant(Timber.DebugTree())
            Timber.i("Привет")

            BT.init(context)
            BT.getPairedDevices()
            BT.autoconnect(context)

            shared = context.getSharedPreferences("size", Context.MODE_PRIVATE)
            console_text.value = shared.getString("size", "12")?.toInt()?.sp ?: 12.sp

            //MARK: Вывод символа энтер
            isCheckedUseLiteralEnter = shared.getBoolean("enter", false)

            //MARK: Вывод номера строки
            console.lineVisible = shared.getBoolean("lineVisible", true)

            //Создаем список цветов из Json цветов
            colorJsonToList()

            // Initialize DNS-SD service discovery
            nsdHelper?.initializeNsd()

            // Start looking for available audio channels in the network
            nsdHelper?.discoverServices()

            ipAddress = readLocalIP(context)
            Timber.i(ipAddress)

            val udp = UDP(8888, channelNetworkIn)
            GlobalScope.launch(
                Dispatchers.IO
            ) {
                udp.receiveScope()
            }


            decoder.run()
            decoder.addCmd("pong") {

            }



            val version = BuildConfig.VERSION_NAME

            //Нужно добавить ее в список лази как текущую
            console.messages.add(
                LineTextAndColor(
                    text = "Первый нах",
                    pairList =
                    listOf<PairTextAndColor>(
                        PairTextAndColor(
                            text = " RTT ",
                            colorText = Color(0xFFFFAA00),
                            colorBg = Color(0xFF812C12)
                        ),
                        PairTextAndColor(
                            text = " Terminal ",
                            colorText = Color(0xFFC6D501),
                            colorBg = Color(0xFF587C2F)
                        ),
                        PairTextAndColor(
                            text = " $version ",
                            colorText = Color(0xFF00E2FF),
                            colorBg = Color(0xFF334292)
                        ),
                        PairTextAndColor(
                            text = ">",
                            colorText = Color(0),
                            colorBg = Color(0xFFFF0000)
                        ),
                        PairTextAndColor(
                            text = "!",
                            colorText = Color(0),
                            colorBg = Color(0xFFFFCC00)
                        ),
                        PairTextAndColor(
                            text = ">",
                            colorText = Color(0),
                            colorBg = Color(0xFF339900)
                        ),
                        PairTextAndColor(
                            text = ">",
                            colorText = Color(0),
                            colorBg = Color(0xFF0033CC),
                            flash = true
                        )
                    )
                )
            )

            console.consoleAdd("") //Пустая строка

        }

        isInitialised = true

    }


}