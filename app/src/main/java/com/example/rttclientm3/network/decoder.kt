package com.example.rttclientm3.network

import com.example.rttclientm3.colorline_and_text
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext
import timber.log.Timber

class NetCommandDecoder(
    private val channelIn: Channel<String>,
    private val channelOutCommand: Channel<NetCommand>,
    private val channelOutLastString: Channel<NetCommand>
) {

    private var lastString: String = "" //Прошлая строка

    suspend fun decodeScope() = withContext(Dispatchers.IO) {

        val bigStr: StringBuilder =
            StringBuilder()//Большая строка в которую и складируются данные с канала

        while (true) {

            var string =
                channelIn.receive() //Получить строку с канала, может содежать несколько строк



            string = string.replace('\r', '▒')

            Timber.e( "in>>>${string.length} "+string )

            if (string.isEmpty()) continue

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
                    channelOutCommand.send(NetCommand(lastString, true))
                    channelOutLastString.send(NetCommand(lastString, true))
                    Timber.i( "out>>>${lastString.length} "+lastString )
                    lastString = ""


                } else {
                    //Конец строки не найден
                    //MARK: Тут для дополнения прошлой строки
                    //Получить полную запись посленней строки
                    lastString += bigStr
                    if(lastString.isNotEmpty()){
                        channelOutLastString.send(NetCommand(lastString, false))
                        Timber.w( "out>>>${lastString.length} "+lastString )
                    }
                    bigStr.clear() //Он отжил свое)
                    break
                }

            }


        }


    }


}