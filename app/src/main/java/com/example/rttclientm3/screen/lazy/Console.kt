package com.example.rttclientm3.screen.lazy

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.GenericFontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.rttclientm3.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class PairTextAndColor(
    var text: String,
    var colorText: Color,
    var colorBg: Color,
    var bold: Boolean = false,
    var italic: Boolean = false,
    var underline: Boolean = false,
    var flash: Boolean = false
)

data class LineTextAndColor(
    var text: String, //Строка вообще
    var pairList: List<PairTextAndColor> //То что будет определено в этой строке
)

//var manual_recomposeLazy = mutableStateOf(0)

//println("Индекс первого видимого элемента = " + lazyListState.firstVisibleItemIndex.toString())
//println("Смещение прокрутки первого видимого элемента = " + lazyListState.firstVisibleItemScrollOffset.toString())
//println("Количество строк выведенных на экран lastIndex = " + lazyListState.layoutInfo.visibleItemsInfo.lastIndex.toString())

@SuppressLint("MutableCollectionMutableState")
class Console {


    private var recompose = MutableStateFlow(0)

    var lineVisible by mutableStateOf(false)

    var tracking by mutableStateOf(true) //Слежение за последним полем
    var lastCount by mutableIntStateOf(0)


    val _messages = MutableStateFlow<SnapshotStateList<LineTextAndColor>>(mutableStateListOf())
    val messages: StateFlow<SnapshotStateList<LineTextAndColor>> = _messages

    /**
     *  # Настройка шрифтов
     *  ### Размер шрифта
     */
    private var fontSize by mutableStateOf(12.sp)

    /**
     * ### Используемый шрифт
     */
    private var fontFamily = FontFamily(Font(R.font.jetbrains, FontWeight.Normal))
    //FontFamily.Monospace


    //val messages = mutableStateListOf<LineTextAndColor>()


    //val messages = MutableStateFlow(emptyList<LineTextAndColor>().toMutableList())


    /**
     * # ⛏️ Рекомпозиция списка
     */
    fun recompose() {
        recompose.value++
    }

    var lastVisibleItemIndex = 0


    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun lazy(modifier: Modifier = Modifier) {

        val list = messages.collectAsState()

        println("recompose lazy")

        recompose.collectAsState().value

        //val update by remember { mutableStateOf(true) }  //для мигания

        val lazyListState: LazyListState = rememberLazyListState()


        //println("Последний видимый индекс = $lastVisibleItemIndex")

        //LaunchedEffect(key1 = messagesR) {
        //lastVisibleItemIndex =
        //    lazyListState.layoutInfo.visibleItemsInfo.lastIndex + lazyListState.firstVisibleItemIndex
        //println("lazy lastVisibleItemIndex $lastVisibleItemIndex")
        //}


//        LaunchedEffect(key1 = messagesR) {
//            while (true) {
//                delay(700L)
//                //update = !update
//                recompose++
//                //////////////////////telnetWarning.value = (telnetSlegenie.value == false) && (messages.size > lastCount)
//            }
//        }

//        LaunchedEffect(key1 = lastVisibleItemIndex) {
//            while (true) {
//                delay(200L)
//                val s = messagesR.size
//                if ((s > 20) && tracking) {
//                    lazyListState.scrollToItem(index = messagesR.size - 1) //Анимация (плавная прокрутка) к данному элементу.
//                }
//            }
//        }

//        Column(
//            Modifier
//                .fillMaxSize()
//                .background(Color(0xFF090909))
//                .then(modifier)
//        ) {
//            Box(
//                Modifier
//                    .fillMaxSize()
//                //.weight(1f)
//            )
//            {


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF090909))
                .then(modifier)
//                        .scrollbar(
//                            count = messagesR.count { it.pairList.isNotEmpty() },
//                            lazyListState,
//                            horizontal = false,
//                            countCorrection = 0,
//                            hiddenAlpha = 0f
//                        )
            , state = lazyListState
        )
        {

            //if (_messages.isNotEmpty())
            itemsIndexed(list.value)
            { index, item ->

                if (index < list.value.size)
                    Row(verticalAlignment = Alignment.Top)
                    {

                        val s = item.pairList.size
                        if ((s > 0) && (lineVisible)) {

                            val str: String = when (index) {
                                in 0..9 -> String.format("   %d>", index)
                                in 10..99 -> String.format("  %d>", index)
                                in 100..999 -> String.format(" %d>", index)
                                else -> String.format("%d>", index)
                            }

                            Text(
                                text = str,
                                color = if (item.pairList.isEmpty()) Color.DarkGray else Color.Gray,
                                fontSize = fontSize,
                                fontFamily = fontFamily,
                                lineHeight = fontSize * 1.2f
                            )
                        }

                        for (i in 0 until s) {

                            Text(
                                text = item.pairList[i].text,
                                color = if (!item.pairList[i].flash)
                                    item.pairList[i].colorText
                                else
                                //if (update) item.pairList[i].colorText else Color(0xFF090909)
                                    Color(0xFF090909),
                                modifier = Modifier
                                    .background(
                                        if (!item.pairList[i].flash) item.pairList[i].colorBg else
                                        //if (update) item.pairList[i].colorBg else Color(0xFF090909)
                                            Color(0xFF090909)
                                    ),
                                textDecoration = if (item.pairList[i].underline) TextDecoration.Underline else null,
                                fontWeight = if (item.pairList[i].bold) FontWeight.Bold else null,
                                fontStyle = if (item.pairList[i].italic) FontStyle.Italic else null,
                                fontSize = fontSize,
                                fontFamily = fontFamily,
                                lineHeight = fontSize * 1.2f
                            )

                        }
                    }
            }



            //               }


            // }
        }

    }

    fun consoleAdd(text: String, color: Color = Color.Green, bgColor: Color = Color.Black) {
        if ((_messages.value.size > 0) && (_messages.value.last().text == " ")) {
            _messages.value.removeAt(_messages.value.lastIndex)
            _messages.value.add(
                LineTextAndColor(
                    text,
                    listOf(PairTextAndColor(text = text, color, bgColor))
                )
            )
        } else {
            _messages.value.add(
                LineTextAndColor(
                    text,
                    listOf(PairTextAndColor(text = text, color, bgColor))
                )
            )
        }
    }


    //➕️ ✅️✏️⛏️ $${\color{red}Red}$$ 📥 📤  📃  📑 📁 📘 🇷🇺 🆗 ✳️


    /**
     * # -------------------------------------------------------------------
     * ## 🔧 Установка типа шрифта
     * 📥 **FontFamily(Font(R.font.jetbrains, FontWeight.Normal))**
     */
    fun setFontFamily(fontFamily: GenericFontFamily) {
        this.fontFamily = fontFamily
    }

    /**
     * # -------------------------------------------------------------------
     * ## 🔧 Установка размера шрифта
     */
    fun setFontSize(size: Int) {
        fontSize = size.sp
    }


}









