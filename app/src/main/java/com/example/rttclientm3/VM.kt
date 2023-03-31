package com.example.rttclientm3

import android.net.nsd.NsdServiceInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rttclientm3.screen.consoleAdd
import com.example.rttclientm3.screen.manual_recomposeLazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket

class VM : ViewModel() {





    // Declare NsdHelper object for service discovery
    private val nsdHelper: NsdHelper? = object : NsdHelper(contex!!) {
        override fun onNsdServiceResolved(service: NsdServiceInfo) {
            // A new network service is available
            // Put your custom logic here!!!
        }

        override fun onNsdServiceLost(service: NsdServiceInfo) {
            // A network service is no longer available
            // Put your custom logic here!!!
        }
    }

    // Block that is run when the view model is created
    init {

        // Initialize DNS-SD service discovery
        nsdHelper?.initializeNsd()

        // Start looking for available audio channels in the network
        nsdHelper?.discoverServices()

    }


    //Канал передачи
    private val channel = Channel<String>(1000000)

    fun launchUDPRecive() {
        viewModelScope.launch {
            UDPRoutine()
        }
    }

    fun launchUIChanelRecive() {
        viewModelScope.launch {
            reciveUI()
        }
    }

    //Создание списка pairTextAndColor из исходного текста
    private fun text_to_paitList(txt: String): List<pairTextAndColor> {
        val pair: MutableList<pairTextAndColor> = arrayListOf()
        //замена [ на \u001C это и будет новый разделитель
        val str = txt.replace("\u001B", "\u001C\u001B")
        val list = str.split("\u001C") //Разделить по 1C чтобы сохранить [
        for (str1 in list) {
            if (str1 == "") {
                continue
            } else {
                //println("!text_to_paitList! split по ESC >>$str1")
                val p = stringcalculate(str1)
                pair += p//Создаем список пар для одной строки
            }
        }
        return pair
    }

    private suspend fun reciveUI() = withContext(Dispatchers.Main)
    {
        val bigStr: StringBuilder =
            StringBuilder()//Большая строка в которую и складируются данные с канала

        while (true) {

            val string =
                channel.receive() //Получить строку с канала, сможет соделжать несколько строк

            bigStr.append(string) //Захерячиваем в большую строку

            val stringCorrection = string.replace('\n', '▒')
            //println("!reciveUI!>>Из канала>>$string")
            println("!reciveUI!>>Из канала Коррекция>>$stringCorrection")

            //println("!reciveUI!>>BigStr>>$bigStr")
            //println("!reciveUI! Есть \\n = ${bigStr.indexOf('\n')} Длинна ${bigStr.length}")

            val strList = mutableListOf<String>()

            //MARK: Будем сами делить на строки
            do {
                //Индекс \n
                val indexN = bigStr.indexOf('\n')

                if (indexN != -1) {
                    //Область по любому имет конец строки
                    //MARK: Чета есть, копируем в подстроку
                    val stringDoN = bigStr.substring(0, indexN)
                    bigStr.delete(0, bigStr.indexOf('\n') + 1)
                    strList.add(stringDoN)

                    //MARK: Тут для дополнения прошлой строки
                    //Получить полную запись посленней строки
                    colorline_and_text.last().text += stringDoN

                    if (isCheckedUseLiteralEnter.value)
                      colorline_and_text.last().text += '⤵'

                    //println("!reciveUI! Старая строка >>${colorline_and_text.last().text}")

                    //Создать список из строки основе текста
                    val pair = text_to_paitList(colorline_and_text.last().text)
                    colorline_and_text.last().pairList = pair

                    manual_recomposeLazy.value =
                        manual_recomposeLazy.value + 1 //Для ручной рекомпозиции списка

                    consoleAdd("") //Пустая строка

                } else {

                    //MARK: Тут для дополнения прошлой строки
                    //Получить полную запись посленней строки
                    colorline_and_text.last().text += bigStr
                    bigStr.clear() //Он отжил свое)

                    //println("!reciveUI! Старая строка >>${colorline_and_text.last().text}")

                    //Создать список из строки основе текста
                    val pair = text_to_paitList(colorline_and_text.last().text)
                    colorline_and_text.last().pairList = pair

                    manual_recomposeLazy.value =
                        manual_recomposeLazy.value + 1 //Для ручной рекомпозиции списка

                    break
                }
            } while (true)


        }
    }

    private suspend fun UDPRoutine() = withContext(Dispatchers.IO) {
        println("Запуск UDPRoutine ")
        val buffer = ByteArray(1024 * 128)
        var socket: DatagramSocket? = null
        socket = DatagramSocket(8888)
        socket.broadcast = true
        val packet = DatagramPacket(buffer, buffer.size)
        socket.receiveBufferSize = 1024 * 512
        while (true) {
            socket.receive(packet)
            val buffer1: ByteArray = packet.data.copyOfRange(0, packet.length)
            val string = String(buffer1)
            //println("!UDPRoutine! packet RAW=[$string")
            channel.send(string)
        }
    }
}