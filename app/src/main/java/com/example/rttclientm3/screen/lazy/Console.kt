package com.example.rttclientm3.screen.lazy

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.GenericFontFamily
import androidx.compose.ui.unit.sp
import com.example.rttclientm3.R
import com.example.rttclientm3.ScriptItemDraw
import com.example.rttclientm3.console
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import libs.modifier.scrollbar
import timber.log.Timber
import java.lang.Exception

var update = MutableStateFlow(true)   //для мигания


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
    var pairList: List<PairTextAndColor>, //То что будет определено в этой строке
    var deleted: Boolean = false
)

//var manual_recomposeLazy = mutableStateOf(0)

//println("Индекс первого видимого элемента = " + lazyListState.firstVisibleItemIndex.toString())
//println("Смещение прокрутки первого видимого элемента = " + lazyListState.firstVisibleItemScrollOffset.toString())
//println("Количество строк выведенных на экран lastIndex = " + lazyListState.layoutInfo.visibleItemsInfo.lastIndex.toString())

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("MutableCollectionMutableState")
class Console {

    init {

        GlobalScope.launch {
            while (true) {
                delay(700L)
                update.value = !update.value
            }
        }

    }


    private var recompose = MutableStateFlow(0)

    var lineVisible by mutableStateOf(false)

    var tracking by mutableStateOf(true) //Слежение за последним полем
    var lastCount by mutableIntStateOf(0)


    val _messages = mutableListOf<LineTextAndColor>()

    //val messages: StateFlow<SnapshotStateList<LineTextAndColor>> = _messages

    //val _messages = MutableList<LineTextAndColor>(emptyList<LineTextAndColor>())

    /**
     *  # Настройка шрифтов
     *  ### Размер шрифта
     */
    var fontSize by mutableStateOf(12.sp)

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

    private val lazyListState: LazyListState = LazyListState()

    fun clear() {

        _messages.clear()

        _messages.forEach {
            it.deleted = true
        }

        _messages.add(
            LineTextAndColor(
                "...",
                listOf(PairTextAndColor("///", Color.Red, Color.Green))
            )
        )
        recompose()
    }


    @Composable
    fun lazy(modifier: Modifier = Modifier) {

        update.collectAsState().value
        recompose.collectAsState().value

        val list = _messages //.filter { !it.deleted }

        //var update by remember { mutableStateOf(true) }  //для мигания

        //val lazyListState: LazyListState = rememberLazyListState()


        //println("Последний видимый индекс = $lastVisibleItemIndex")

        //LaunchedEffect(key1 = messagesR) {
        //lastVisibleItemIndex =
        //    lazyListState.layoutInfo.visibleItemsInfo.lastIndex + lazyListState.firstVisibleItemIndex
        //println("lazy lastVisibleItemIndex $lastVisibleItemIndex")
        //}


//        LaunchedEffect(key1 = list) {
//            while (true) {
//                delay(700L)
//                //update = !update
//                //recompose.value++
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


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF090909)),
            //.then(modifier)
//                        .scrollbar(
//                            count = list.count { it.pairList.isNotEmpty() },
//                            lazyListState,
//                            horizontal = false,
//                            countCorrection = 0,
//                            hiddenAlpha = 0f
//                        )
            //state = lazyListState
        )
        {


            itemsIndexed(list)
            { index, item ->
                if (!item.deleted)
                    ScriptItemDraw({ item }, { index }, { false })
            }


            //if (messages.value.isNotEmpty())
//            itemsIndexed(messages.value)
//            { index, item ->
//                //update.collectAsState().value
//                //recompose.collectAsState().value
//                //ScriptItemDraw({ item }, { index }, { false })
//            }

        }
    }

    fun consoleAdd(
        text: String,
        color: Color = Color.Green,
        bgColor: Color = Color.Black,
        flash: Boolean = false
    ) {
        if ((_messages.size > 0) && (_messages.last().text == " ")) {
            _messages.removeAt(_messages.lastIndex)
            _messages.add(
                LineTextAndColor(
                    text,
                    listOf(PairTextAndColor(text = text, color, bgColor, flash = flash))
                )
            )
        } else {
            _messages.add(
                LineTextAndColor(
                    text,
                    listOf(PairTextAndColor(text = text, color, bgColor, flash = flash))
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










