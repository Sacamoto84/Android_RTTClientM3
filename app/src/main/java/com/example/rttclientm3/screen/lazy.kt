package com.example.rttclientm3.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.rttclientm3.R
import com.example.rttclientm3.ScriptItemDraw
import com.example.rttclientm3.bottomNavigationLazy
import com.example.rttclientm3.colorline_and_text
import com.example.rttclientm3.console_text
import com.example.rttclientm3.isCheckedUselineVisible
import com.example.rttclientm3.lastCount
import com.example.rttclientm3.lineTextAndColor
import com.example.rttclientm3.pairTextAndColor
import com.example.rttclientm3.telnetSlegenie
import com.example.rttclientm3.telnetWarning
import kotlinx.coroutines.delay

var manual_recomposeLazy = mutableStateOf(0)

@Composable
fun lazy(navController: NavController, messages: SnapshotStateList<lineTextAndColor>) {
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
                    //.simpleHorizontalScrollbar(lazyListState)
                    .scrollbar(lazyListState, horizontal = false, countCorrection = 0, hiddenAlpha = 0f),
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
//                            else {
//                                if (s == 0) {
//                                    Text(
//                                        text = "",
//                                        color = if (item.pairList.isEmpty()) Color.DarkGray else Color.Gray,
//                                        fontSize = console_text.value,
//                                        fontFamily = FontFamily(
//                                            Font(R.font.jetbrains, FontWeight.Normal)
//                                        )
//                                    )
//                                }
//                            }

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
        bottomNavigationLazy(navController as NavHostController)
    }
}

fun consoleAdd(text: String, color: Color = Color.Green, bgColor: Color = Color.Black) {

    if (colorline_and_text.last().text == " ") {
        colorline_and_text.removeAt(colorline_and_text.lastIndex)

        colorline_and_text.add(
            lineTextAndColor(
                text,
                listOf(
                    pairTextAndColor(
                        text = text,
                        color,
                        bgColor
                    )
                )
            )
        )

    } else {
        colorline_and_text.add(
            lineTextAndColor(
                text,
                listOf(
                    pairTextAndColor(
                        text = text,
                        color,
                        bgColor
                    )
                )
            )
        )
    }


}

@Composable
fun Modifier.simpleHorizontalScrollbar(
    state: LazyListState,
    height: Float = 12f,
    backgroundColor: Color = Color.Blue,
    color: Color = Color.Red
): Modifier {

    return drawWithContent {
        drawContent()

        val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index

        if (firstVisibleElementIndex != null) {

            val scrollableItems =
                state.layoutInfo.totalItemsCount - state.layoutInfo.visibleItemsInfo.size
            val scrollBarHeight = this.size.height / scrollableItems
            val offsetY =
                ((this.size.height - scrollBarHeight) * firstVisibleElementIndex) / scrollableItems

            drawRect(
                color = backgroundColor,
                topLeft = Offset(x = this.size.width - 10.dp.toPx(), y = offsetY),
                size = Size(10.dp.toPx(), scrollBarHeight),
                alpha = 1f
            )

//            drawRect(
//                color = color,
//                topLeft = Offset(x = offsetX, y = this.size.height),
//                size = Size(scrollBarWidth, height),
//                alpha = 1f
//            )
        }
    }
}

/**
 * Renders a scrollbar.
 *
 * <ul> <li> A scrollbar is composed of two components: a track and a knob. The knob moves across
 * the track <li> The scrollbar appears automatically when the user starts scrolling and disappears
 * after the scrolling is finished </ul>
 *
 * @param state The [LazyListState] that has been passed into the lazy list or lazy row
 * @param horizontal If `true`, this will be a horizontally-scrolling (left and right) scroll bar,
 * if `false`, it will be vertically-scrolling (up and down)
 * @param alignEnd If `true`, the scrollbar will appear at the "end" of the scrollable composable it
 * is decorating (at the right-hand side in left-to-right locales or left-hand side in right-to-left
 * locales, for the vertical scrollbars -or- the bottom for horizontal scrollbars). If `false`, the
 * scrollbar will appear at the "start" of the scrollable composable it is decorating (at the
 * left-hand side in left-to-right locales or right-hand side in right-to-left locales, for the
 * vertical scrollbars -or- the top for horizontal scrollbars)
 * @param thickness How thick/wide the track and knob should be
 * @param fixedKnobRatio If not `null`, the knob will always have this size, proportional to the
 * size of the track. You should consider doing this if the size of the items in the scrollable
 * composable is not uniform, to avoid the knob from oscillating in size as you scroll through the
 * list
 * @param knobCornerRadius The corner radius for the knob
 * @param trackCornerRadius The corner radius for the track
 * @param knobColor The color of the knob
 * @param trackColor The color of the track. Make it [Color.Transparent] to hide it
 * @param padding Edge padding to "squeeze" the scrollbar start/end in so it's not flush with the
 * contents of the scrollable composable it is decorating
 * @param visibleAlpha The alpha when the scrollbar is fully faded-in
 * @param hiddenAlpha The alpha when the scrollbar is fully faded-out. Use a non-`0` number to keep
 * the scrollbar from ever fading out completely
 * @param fadeInAnimationDurationMs The duration of the fade-in animation when the scrollbar appears
 * once the user starts scrolling
 * @param fadeOutAnimationDurationMs The duration of the fade-out animation when the scrollbar
 * disappears after the user is finished scrolling
 * @param fadeOutAnimationDelayMs Amount of time to wait after the user is finished scrolling before
 * the scrollbar begins its fade-out animation
 */
fun Modifier.scrollbar(
    state: LazyListState,
    horizontal: Boolean,
    alignEnd: Boolean = true,
    thickness: Dp = 4.dp,
    fixedKnobRatio: Float? = null,
    knobCornerRadius: Dp = 4.dp,
    trackCornerRadius: Dp = 2.dp,
    knobColor: Color = Color.Gray,
    trackColor: Color = Color.DarkGray,
    padding: Dp = 0.dp,
    visibleAlpha: Float = 1f,
    hiddenAlpha: Float = 0f,
    fadeInAnimationDurationMs: Int = 150,
    fadeOutAnimationDurationMs: Int = 500,
    fadeOutAnimationDelayMs: Int = 1000,
    countCorrection: Int = 0
): Modifier = composed {
    check(thickness > 0.dp) { "Thickness must be a positive integer." }
    check(fixedKnobRatio == null || fixedKnobRatio < 1f) {
        "A fixed knob ratio must be smaller than 1."
    }
    check(knobCornerRadius >= 0.dp) { "Knob corner radius must be greater than or equal to 0." }
    check(trackCornerRadius >= 0.dp) { "Track corner radius must be greater than or equal to 0." }
    check(hiddenAlpha <= visibleAlpha) { "Hidden alpha cannot be greater than visible alpha." }
    check(fadeInAnimationDurationMs >= 0) {
        "Fade in animation duration must be greater than or equal to 0."
    }
    check(fadeOutAnimationDurationMs >= 0) {
        "Fade out animation duration must be greater than or equal to 0."
    }
    check(fadeOutAnimationDelayMs >= 0) {
        "Fade out animation delay must be greater than or equal to 0."
    }

    val targetAlpha = if (state.isScrollInProgress) {
        visibleAlpha
    } else {
        hiddenAlpha
    }
    val animationDurationMs = if (state.isScrollInProgress) { fadeInAnimationDurationMs } else { fadeOutAnimationDurationMs }

    val animationDelayMs = if (state.isScrollInProgress) {
        0
    } else {
        fadeOutAnimationDelayMs
    }

    val alpha by
    animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec =
        tween(delayMillis = animationDelayMs, durationMillis = animationDurationMs)
    )

    val debug = true

    drawWithContent {
        drawContent()

        state.layoutInfo.visibleItemsInfo.firstOrNull()?.let { firstVisibleItem ->

            if (state.isScrollInProgress || alpha > 0f) {

                // Size of the viewport, the entire size of the scrollable composable we are decorating with
                // this scrollbar.
                val viewportSize =
                    if (horizontal) {
                        size.width
                    } else {
                        size.height
                    } - padding.toPx() * 2


                //Размер первого видимого элемента. Мы используем это, чтобы оценить, сколько элементов может поместиться в окне просмотра.
                //Конечно, это работает идеально, когда все элементы одинакового размера.
                //Когда это не так, размер ползунка прокрутки увеличивается и уменьшается при прокрутке.
                val firstItemSize = firstVisibleItem.size

                // Используем это, чтобы оценить, сколько элементов может вместиться в окно просмотра.
                val count = colorline_and_text.count { it.pairList.isNotEmpty() }

                val estimatedFullListSize = firstItemSize * (
                        count
                        //state.layoutInfo.totalItemsCount
                        //colorline_and_text.count()
                        //+ if (colorline_and_text.last().pairList.isEmpty()) {-1} else 0
                        )

                //Разница в положении между первыми пикселями, видимыми в нашем окне просмотра при прокрутке, и верхней частью полностью заполненного
                //прокручиваемого составного, если бы он показывал все элементы одновременно. Сначала значение будет равно 0,
                //поскольку мы начинаем с самого верха (или с начальной границы). При прокрутке вниз (или к концу), это число будет расти.
                val viewportOffsetInFullListSpace =
                    state.firstVisibleItemIndex * firstItemSize + state.firstVisibleItemScrollOffset

                // Где мы должны отрисовать knob в нашем составном.
                //val knobPosition = (viewportSize / estimatedFullListSize) * viewportOffsetInFullListSpace + padding.toPx()

                val knobPosition =
                    viewportSize * (viewportOffsetInFullListSpace.toFloat() / estimatedFullListSize.toFloat()) + padding.toPx()
                if (debug) println("viewportSize $viewportSize")
                if (debug) println("knobPosition $knobPosition")


                // How large should the knob be.
                val knobSize =
                    fixedKnobRatio?.let { it * viewportSize }
                        ?: ((viewportSize * viewportSize) / estimatedFullListSize.toFloat())


                // Draw the track
                drawRoundRect(
                    color = trackColor,
                    topLeft =

                    when {

                        // When the scrollbar is horizontal and aligned to the bottom:
                        horizontal && alignEnd -> Offset(
                            padding.toPx(),
                            size.height - thickness.toPx()
                        )

                        // When the scrollbar is horizontal and aligned to the top:
                        horizontal && !alignEnd -> Offset(padding.toPx(), 0f)
                        // When the scrollbar is vertical and aligned to the end:
                        alignEnd -> Offset(size.width - thickness.toPx(), padding.toPx())
                        // When the scrollbar is vertical and aligned to the start:
                        else -> Offset(0f, padding.toPx())
                    },

                    size =
                    if (horizontal) {
                        Size(size.width - padding.toPx() * 2, thickness.toPx())
                    } else {
                        Size(thickness.toPx(), size.height - padding.toPx() * 2)
                    },

                    alpha = alpha,

                    cornerRadius = CornerRadius(
                        x = trackCornerRadius.toPx(),
                        y = trackCornerRadius.toPx()
                    ),
                )

                // Draw the knob
                drawRoundRect(
                    color = knobColor,
                    topLeft =
                    when {
                        // When the scrollbar is horizontal and aligned to the bottom:
                        horizontal && alignEnd -> Offset(
                            knobPosition,
                            size.height - thickness.toPx()
                        )
                        // When the scrollbar is horizontal and aligned to the top:
                        horizontal && !alignEnd -> Offset(knobPosition, 0f)
                        // When the scrollbar is vertical and aligned to the end:
                        alignEnd -> Offset(size.width - thickness.toPx(), knobPosition)
                        // When the scrollbar is vertical and aligned to the start:
                        else -> Offset(0f, knobPosition)
                    },
                    size =
                    if (horizontal) {
                        Size(knobSize, thickness.toPx())
                    } else {

                        Size(thickness.toPx(), knobSize)

                    },
                    alpha = alpha,
                    cornerRadius = CornerRadius(
                        x = knobCornerRadius.toPx(),
                        y = knobCornerRadius.toPx()
                    ),
                )
            }

        }
    }
}
