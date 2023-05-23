package com.example.rttclientm3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import libs.console.PairTextAndColor
import com.example.rttclientm3.network.channelLastString
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
    private fun text_to_paitList(txt: String): List<PairTextAndColor> {
        val pair: MutableList<PairTextAndColor> = arrayListOf()
        //замена [ на \u001C это и будет новый разделитель
        val str = txt.replace("\u001B", "\u001C\u001B")
        val list = str.split("\u001C") //Разделить по 1C чтобы сохранить [
        for (str1 in list) {
            if (str1 == "") {
                continue
            } else {
                //println("!text_to_paitList! split по ESC >>$str1")
                val p = stringcalculate(str1)
                pair.addAll(p)//Создаем список пар для одной строки
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
                if (isCheckedUseLiteralEnter) s.cmd += '⤵'
                val pair = text_to_paitList(s.cmd)
                console.messages.last().text = s.cmd
                console.messages.last().pairList = pair
                if (s.newString) console.consoleAdd("")
            }

            withContext(Dispatchers.Main)
            {
                //Timber.i("Ку ${channelLastString.isEmpty} ${colorline_and_text.size} ${colorline_and_text.last().text}")
                console.recompose() //Для ручной композиции списка
            }

        }
    }

}