package com.example.rttclientm3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.example.rttclientm3.screen.lazy.LineTextAndColor
import com.example.rttclientm3.screen.lazy.update

@Composable
fun ScriptItemDraw(item: () -> LineTextAndColor, index: () -> Int, select: () -> Boolean) {

    println("Draw  ${index()}")

    val x = convertStringToAnnotatedString(item(), index())
    Text( x, modifier = Modifier
            .fillMaxWidth()
            //.padding(top = 0.dp)
            .background(if (select()) Color.Cyan else Color.Transparent),

        fontSize = console.fontSize,
        fontFamily = FontFamily(Font(R.font.jetbrains, FontWeight.Normal)),
        //lineHeight = console.fontSize * 1.2f
    )

}

private fun convertStringToAnnotatedString(item: LineTextAndColor, index: Int): AnnotatedString {


    val s = item.pairList.size

    //lateinit var x : AnnotatedString
    var x = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Gray)) {
            append("${index}>")
        }
    }

    for (i in 0 until s) {

        x += buildAnnotatedString {
            withStyle(
                style = SpanStyle(

                    color = if (!item.pairList[i].flash)
                        item.pairList[i].colorText
                    else
                        if (update.value)
                            item.pairList[i].colorText
                        else
                            Color(0xFF090909),

                    background = if (!item.pairList[i].flash)
                        item.pairList[i].colorBg
                    else
                        if (update.value)
                            item.pairList[i].colorBg
                        else Color(0xFF090909),
                    fontFamily = FontFamily(Font(R.font.jetbrains)),

                    textDecoration = if (item.pairList[i].underline) TextDecoration.Underline else null,
                    fontWeight = if (item.pairList[i].bold) FontWeight.Bold else null,
                    fontStyle = if (item.pairList[i].italic) FontStyle.Italic else null,
                )
            )
            { append(item.pairList[i].text) }
        }

    }

    return x
}


