package com.example.rttclientm3

import android.net.nsd.NsdServiceInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rttclientm3.network.UDP
import com.example.rttclientm3.network.channelLastString
import com.example.rttclientm3.network.channelNetworkIn
import com.example.rttclientm3.screen.lazy.consoleAdd
import com.example.rttclientm3.screen.lazy.manual_recomposeLazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VM : ViewModel() {

    fun launchUIChanelRecive() {

        viewModelScope.launch(Dispatchers.IO) {
            receiveUILastString()
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun receiveUILastString() {
        while (true) {

            while (!channelLastString.isEmpty)
            {

                val s = channelLastString.receive()
                if(s.cmd == "") continue
                if (isCheckedUseLiteralEnter.value) s.cmd += '⤵'
                val pair = text_to_paitList(s.cmd)

                colorline_and_text.last().text = s.cmd
                colorline_and_text.last().pairList = pair
                if (s.newString) consoleAdd("")

            }

            withContext(Dispatchers.Main)
            {
                //Timber.i("Ку ${channelLastString.isEmpty} ${colorline_and_text.size} ${colorline_and_text.last().text}")
                manual_recomposeLazy.value = manual_recomposeLazy.value + 1 //Для ручной рекомпозиции списка
            }

        }
    }

}