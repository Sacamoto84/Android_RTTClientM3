package com.example.rttclientm3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import libs.console.LineTextAndColor

@Composable
fun ScriptItemDraw(item: () -> LineTextAndColor, index: () -> Int, select: () -> Boolean) {

    println("Draw  ${index()}")

    val x = convertStringToAnnotatedString(item(), index())
    Text(
        x,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp)
            .background(if (select()) Color.Cyan else Color.Transparent),
        fontSize = 20.sp
    )

}

private fun convertStringToAnnotatedString(item: LineTextAndColor, index: Int): AnnotatedString {

    val s = item.pairList.size

    //lateinit var x : AnnotatedString
    var x = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Blue, background = Color.White)) {
            append("$index")
        }
    }

    for (i in 0 until s) {

        x += buildAnnotatedString {
            withStyle(
                style = SpanStyle(

                    color = item.pairList[i].colorText,
                    background = item.pairList[i].colorBg,
                    fontFamily = FontFamily(Font(R.font.jetbrains)),

                    textDecoration = if (item.pairList[i].underline) TextDecoration.Underline else null,
                    fontWeight = if (item.pairList[i].bold) FontWeight.Bold else null,
                    fontStyle = if (item.pairList[i].italic) FontStyle.Italic else null,

                    fontSize = console_text.value,
                )
            )
            { append(item.pairList[i].text) }
        }

    }

    return x
}


