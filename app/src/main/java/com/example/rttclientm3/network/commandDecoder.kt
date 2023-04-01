package com.example.rttclientm3.network

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import timber.log.Timber

class CommandDecoder(
    private val channelIn: Channel<NetCommand>, //Канал RAW строки до Enter
    private val channelOut: Channel<String>, //Канал чистая команда
) {

    @OptIn(DelicateCoroutinesApi::class)
    fun run() {
        GlobalScope.launch(Dispatchers.IO)
        {
            while (true) {

                //val raw = channelIn.receive().cmd

                val raw = "qwe!xzassd;45#qwe"

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
                channelOut.send(s)

            }
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


//    uint8_t BLE::CRC8(char *pcBlock, unsigned int len)
//    {
//        unsigned char _crc = 0xFF;
//        unsigned int i;
//
//        while (len--)
//        {
//            _crc ^= *pcBlock++;
//
//            for (i = 0; i < 8; i++)
//            _crc = _crc & 0x80 ? (_crc << 1) ^ 0x31 : _crc << 1;
//        }
//        return _crc;
//    }


}