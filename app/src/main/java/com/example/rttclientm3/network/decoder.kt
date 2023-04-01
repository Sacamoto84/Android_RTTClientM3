package com.example.rttclientm3.network

import com.example.rttclientm3.colorline_and_text
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class NetCommandDecoder(
    private val channelIn: Channel<String>,               //Входной канал от bt и wifi
    private val channelOutCommand: Channel<String>,       //Готовые команды из пакета
    private val channelOutLastString: Channel<NetCommand> //Для Отображения списка текст.новая строка
) {

    private var lastString: String = "" //Прошлая строка

    val channelRoute = Channel<String>(1000000)

    @OptIn(DelicateCoroutinesApi::class)
    fun run()
    {
        Timber.i ("Запуск декодировщика")

        GlobalScope.launch(Dispatchers.IO) {
            decodeScope()
        }

        GlobalScope.launch(Dispatchers.IO)
        {
             commandDecoder()
        }

    }

    suspend fun decodeScope() {

        val bigStr: StringBuilder =
            StringBuilder()//Большая строка в которую и складируются данные с канала

        while (true) {

            var string =
                channelIn.receive() //Получить строку с канала, может содежать несколько строк

            string = string.replace('\r', '▒')

            //Timber.e( "in>>>${string.length} "+string )

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
                    channelRoute.send(lastString)
                    channelOutLastString.send(NetCommand(lastString, true))
                    //Timber.i( "out>>>${lastString.length} "+lastString )
                    lastString = ""


                } else {
                    //Конец строки не найден
                    //MARK: Тут для дополнения прошлой строки
                    //Получить полную запись посленней строки
                    lastString += bigStr
                    if(lastString.isNotEmpty()){
                        channelOutLastString.send(NetCommand(lastString, false))
                        //Timber.w( "out>>>${lastString.length} "+lastString )
                    }
                    bigStr.clear() //Он отжил свое)
                    break
                }

            }


        }


    }








    @OptIn(DelicateCoroutinesApi::class)
    suspend fun commandDecoder() {

            while (true) {

                val raw = channelRoute.receive()

                //val raw = "qwe!xzassd;45#qwe"

                val posStart = raw.indexOf("!")
                val posCRC = raw.indexOf(";")
                val posEnd = raw.indexOf("#")

                if ((posStart == -1) || (posEnd == -1) || (posCRC == -1) || (posCRC !in (posStart + 1) until posEnd)) {
                    Timber.e("Ошибка позиций пакета S:$posStart C:$posCRC E:$posEnd")
                    continue
                }

                if (((posEnd - posCRC) > 4) || ((posEnd - posCRC) == 1)) {
                    Timber.e("S:$posStart C:$posCRC E:$posEnd")
                    Timber.e("L0 > Error > (PosE - PosCRC) > 4 or == 1");
                    continue
                }

                val crcStr = raw.substring(posCRC + 1 until posEnd)
                var crc = 0
                try {
                    crc = crcStr.toInt()
                } catch (e: Exception) {
                    Timber.e("Ошибка преобразования CRC $crcStr")
                    continue
                }

                val s = raw.substring(posStart + 1 until posCRC)
                if(s == "")
                {
                    Timber.e("Нет тела команды $raw")
                    continue
                }
                val crc8 = CRC8(s)

                if (crc.toUByte() != crc8)
                {
                    Timber.e("Ошибка CRC $crc != CRC8 $crc8 $raw")
                    continue
                }
                //Прошли все проверкu
                channelOutCommand.send(s)

            }

    }



    /*
Name  : CRC-8
Poly  : 0x31    x^8 + x^5 + x^4 + 1
Init  : 0xFF
Revert: false
XorOut: 0x00
Check : 0xF7 ("123456789")
MaxLen: 15 байт(127 бит) - обнаружение
одинарных, двойных, тройных и всех нечетных ошибок
*/
    fun CRC8(str : String): UByte
    {

        var _crc: UByte = 0xFFu.toUByte()
        var i: Int

        for (j in str.indices) {
            _crc = _crc xor str[j].code.toUByte()

            for (k in 0 until 8) {
                _crc = if (_crc and 0x80u.toUByte() != 0u.toUByte()) {
                    (_crc.toUInt() shl 1 xor 0x31u.toUInt()).toUByte()
                } else {
                    _crc.toUInt().shl(1).toUByte()
                }
            }
        }
        return _crc
    }

}