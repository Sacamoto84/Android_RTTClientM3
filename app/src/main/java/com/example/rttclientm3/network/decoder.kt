package com.example.rttclientm3.network

import com.example.rttclientm3.colorline_and_text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

class NetCommandDecoder(
    private val channelIn: Channel<String>,
    private val channelOutCommand: Channel<NetCommand>,
    private val channelOutLastString: Channel<String>
) {

    private var lastString: String = "" //Прошлая строка

    suspend fun decodeScope() = withContext(Dispatchers.IO) {

        val bigStr: StringBuilder =
            StringBuilder()//Большая строка в которую и складируются данные с канала

        while (true) {

            var string =
                channelIn.receive() //Получить строку с канала, может содежать несколько строк

            string = string.replace('\r', '▒')

            bigStr.append(string) //Захерячиваем в большую строку



            //MARK: Будем сами делить на строки
            while (true) {
                //Индекс \n
                val indexN = bigStr.indexOf('\n')

                if (indexN != -1) {
                    //Область полюбому имеет конец строки
                    //MARK: Чета есть, копируем в подстроку
                    val stringDoN = bigStr.substring(0, indexN)
                    bigStr.delete(0, bigStr.indexOf('\n') + 1)

                    lastString += stringDoN
                    channelOutCommand.send(NetCommand(lastString))
                    lastString = ""
                    channelOutLastString.send(lastString)

                } else {
                    //Конец строки не найден
                    //MARK: Тут для дополнения прошлой строки
                    //Получить полную запись посленней строки
                    lastString += bigStr
                    channelOutLastString.send(lastString)
                    bigStr.clear() //Он отжил свое)
                    break
                }

            }


        }


    }


}