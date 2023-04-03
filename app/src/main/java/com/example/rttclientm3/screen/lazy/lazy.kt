package com.example.rttclientm3.screen.lazy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.rttclientm3.R
import com.example.rttclientm3.ScriptItemDraw
import com.example.rttclientm3.colorline_and_text
import com.example.rttclientm3.console_text
import com.example.rttclientm3.isCheckedUselineVisible
import com.example.rttclientm3.lastCount
import com.example.rttclientm3.lineTextAndColor
import com.example.rttclientm3.pairTextAndColor
import com.example.rttclientm3.screen.info.modifier.scrollbar
import com.example.rttclientm3.telnetSlegenie
import com.example.rttclientm3.telnetWarning
import kotlinx.coroutines.delay

var manual_recomposeLazy = mutableStateOf(0)

@Composable
fun lazy(navController: NavController, messages: MutableList<lineTextAndColor>) {
    var update by remember { mutableStateOf(true) }  //для мигания
    //println("---lazy---")
    val lazyListState: LazyListState = rememberLazyListState()

    //println("Индекс первого видимого элемента = " + lazyListState.firstVisibleItemIndex.toString())
    //println("Смещение прокрутки первого видимого элемента = " + lazyListState.firstVisibleItemScrollOffset.toString())
    //println("Количество строк выведенных на экран lastIndex = " + lazyListState.layoutInfo.visibleItemsInfo.lastIndex.toString())

    var lastVisibleItemIndex by remember {
        mutableStateOf(0)
    }

    lastVisibleItemIndex =
        lazyListState.layoutInfo.visibleItemsInfo.lastIndex + lazyListState.firstVisibleItemIndex
    //println("Последний видимый индекс = $lastVisibleItemIndex")
    //println("Количество записей = ${messages.size}")

    LaunchedEffect(key1 = messages) {
        while (true) {
            delay(700L)
            update = !update
            telnetWarning.value = (telnetSlegenie.value == false) && (messages.size > lastCount)
        }
    }

    LaunchedEffect(key1 = lastVisibleItemIndex, key2 = messages) {
        while (true) {
            delay(200L)
            val s = messages.size
            if ((s > 20) && (telnetSlegenie.value == true)) {
                lazyListState.scrollToItem(index = messages.size - 1) //Анимация (плавная прокрутка) к данному элементу.
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF090909))
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .weight(1f)
        )
        {

            //Верхний блок Теминала
            ///////////////////////////////////////////////////////////
            LazyColumn(                                              //
                modifier = Modifier
                    .fillMaxSize()
                    .scrollbar( lazyListState, horizontal = false, countCorrection = 0, hiddenAlpha = 0f ),
                state = lazyListState                          //
            ) {

                val z = manual_recomposeLazy.value
                //println("LAZY Счетчик рекомпозиций $z")

                val driver = 0

                itemsIndexed(messages.toList())
                { index, item ->

                    if (driver == 1)
                        ScriptItemDraw({ item }, { index }, { false })
                    else
                        Row()
                        {

                            val s = item.pairList.size

                            if ((s > 0) && (isCheckedUselineVisible.value)) {

                                val str: String = when (index) {
                                    in 0..9 -> String.format("   %d>", index)
                                    in 10..99 -> String.format("  %d>", index)
                                    in 100..999 -> String.format(" %d>", index)
                                    else -> String.format("%d>", index)
                                }
                                Text(
                                    text = str,
                                    color = if (item.pairList.isEmpty()) Color.DarkGray else Color.Gray,
                                    fontSize = console_text.value,
                                    fontFamily = FontFamily(
                                        Font(R.font.jetbrains, FontWeight.Normal)
                                    )
                                )
                            }

                            for (i in 0 until s) {
                                Text(
                                    text = item.pairList[i].text,
                                    color = if (!item.pairList[i].flash)
                                        item.pairList[i].colorText
                                    else
                                        if (update) item.pairList[i].colorText else Color(0xFF090909),
                                    modifier = Modifier.background(
                                        if (!item.pairList[i].flash)
                                            item.pairList[i].colorBg
                                        else
                                            if (update) item.pairList[i].colorBg else Color(
                                                0xFF090909
                                            )
                                    ),
                                    textDecoration = if (item.pairList[i].underline) TextDecoration.Underline else null,
                                    fontWeight = if (item.pairList[i].bold) FontWeight.Bold else null,
                                    fontStyle = if (item.pairList[i].italic) FontStyle.Italic else null,
                                    fontSize = console_text.value,
                                    fontFamily = FontFamily(
                                        Font(R.font.jetbrains, FontWeight.Normal)
                                    )
                                )
                            }


                        }


                }
            }                                                  //
            ///////////////////////////////////////////////////////////
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            )
            {
                val image: Painter = painterResource(id = R.drawable.error)
                val warning by telnetWarning.observeAsState()
                if (warning == true) {
                    Image(
                        painter = image,
                        contentDescription = "",
                        Modifier
                            .size(48.dp)
                            .padding(end = 10.dp)
                    )
                }
            }
        }

        //Блок кнопок
        BottomNavigationLazy(navController as NavHostController)
    }
}

fun consoleAdd(text: String, color: Color = Color.Green, bgColor: Color = Color.Black) {
    if ((colorline_and_text.size > 0) && (colorline_and_text.last().text == " ")) {
        colorline_and_text.removeAt(colorline_and_text.lastIndex)
        colorline_and_text.add( lineTextAndColor( text, listOf( pairTextAndColor( text = text, color, bgColor ) ) ) )
    } else {
        colorline_and_text.add( lineTextAndColor( text, listOf( pairTextAndColor( text = text, color, bgColor ) ) ) )
    }
}