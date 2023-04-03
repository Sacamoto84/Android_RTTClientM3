package com.example.rttclientm3.screen.info.modifier

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.rttclientm3.colorline_and_text

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

    val debug = false

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