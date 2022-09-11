package com.example.rttclientm3

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController


//MARK: Локальные дефайны
val textSize = 12.sp
val fontWeight = FontWeight.Normal
val boxSize = 32.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun info(navController: NavController) {

    val scrollState = rememberScrollState()

    Column(Modifier.fillMaxSize().background(Color(0xFF090909))) {

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .weight(1f)
        )
        {

            Text(
                text = """  \x1B \033 \u001b | 38;05;xxx Text | 48;05;xxx Bg""",
                color = Color.White
            )
            Text(
                text = "  01 Bold | 03 Italic | 04 Underline | 07 Revers | 08 Flash",
                color = Color.White
            )
            Text(text = """  \033[01;03;38;05;147;48;05;21m Текст \033[0m\n""", color = Color.White)
            Text(text = "  Порт 8888 | Очистка экрана \\033[1m ", color = Color.White)

            //Рисуем таблицу
            Column(
                Modifier
                    .fillMaxSize(),
                //verticalArrangement = Arrangement.SpaceBetween
            ) {

                for (i in 0..1) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {

                        for (x in 0..7) {

                            Box(
                                Modifier
                                    .padding(start = 0.5.dp, top = 0.5.dp)
                                    .height(boxSize)
                                    .weight(1f)
                                    .background(colorIn256(x + i * 8)),
                                contentAlignment = Alignment.Center
                            )
                            {
                                val textcolor = when (x + i * 8) {
                                    in 0..4, in 16..27, in 232..243 -> Color(0xFFBBBBBB)
                                    else -> Color.Black
                                }

                                Text(
                                    text = "${x + i * 8}",
                                    color = textcolor,
                                    fontSize = textSize,
                                    fontWeight = fontWeight
                                )
                            }
                        }
                    }
                }

                var index = 16

                for (i in 0..14) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {

                        for (x in 0..15) {

                            Box(
                                Modifier
                                    .height(boxSize)
                                    .padding(start = 0.5.dp, top = 0.5.dp)
                                    .weight(1f)
                                    .background(colorIn256(index)),
                                contentAlignment = Alignment.Center
                            )
                            {


                                val textcolor = when (index) {
                                    in 0..4, in 16..27, in 232..243 -> Color(0xFFBBBBBB)
                                    else -> Color.Black
                                }

                                Text(
                                    text = "${index}",
                                    color = textcolor,
                                    fontSize = textSize,
                                    fontWeight = fontWeight
                                )

                                index++
                            }
                        }
                    }
                }
            }

            val buttonFontSize = 12.sp

            Row(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
                    .height(50.dp)
            ) {

                Button(colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF505050)),
                    onClick = {
                        console_text.value = 12.sp
                        consoleAdd("Изменение шрифта")
                        shared.edit().putString("size", "12").apply()
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                {
                    Text("12", fontSize = buttonFontSize, color = if(console_text.value == 12.sp) Color.LightGray else  Color.Black)
                }

                Button(colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF505050)),
                    onClick = {
                        console_text.value = 14.sp
                        consoleAdd("Изменение шрифта")
                        shared.edit().putString("size", "14").apply()
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = 1.dp)
                )
                {
                    Text("14", fontSize = buttonFontSize, color = if(console_text.value == 14.sp) Color.LightGray else  Color.Black)
                }

                Button(colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF505050)),
                    onClick = {
                        console_text.value = 16.sp
                        consoleAdd("Изменение шрифта")
                        shared.edit().putString("size", "16").apply()
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = 1.dp)
                )
                {
                    Text("16", fontSize = buttonFontSize, color = if(console_text.value == 16.sp) Color.LightGray else  Color.Black)
                }

                Button(colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF505050)),
                    onClick = {
                        console_text.value = 18.sp
                        consoleAdd("Изменение шрифта")
                        shared.edit().putString("size", "18").apply()
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = 1.dp)
                )
                {
                    Text("18", fontSize = buttonFontSize, color = if(console_text.value == 18.sp) Color.LightGray else  Color.Black)
                }

                Button(colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF505050)),
                    onClick = {
                        console_text.value = 20.sp
                        consoleAdd("Изменение шрифта")
                        shared.edit().putString("size", "20").apply()
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = 1.dp)
                )
                {
                    Text("20", fontSize = buttonFontSize, color = if(console_text.value == 20.sp) Color.LightGray else  Color.Black)
                }

                Button(colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF505050)),
                    onClick = {
                        console_text.value = 22.sp
                        consoleAdd("Изменение шрифта")
                        shared.edit().putString("size", "22").apply()
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = 1.dp)
                )
                {
                    Text("22", fontSize = buttonFontSize, color = if(console_text.value == 22.sp) Color.LightGray else  Color.Black)
                }

                Button(colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF505050)),
                    onClick = {
                        console_text.value = 24.sp
                        consoleAdd("Изменение шрифта")
                        shared.edit().putString("size", "24").apply()
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = 1.dp)
                )
                {
                    Text("24", fontSize = buttonFontSize, color = if(console_text.value == 24.sp) Color.LightGray else  Color.Black)
                }

                Button(colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF505050)),
                    onClick = {
                        console_text.value = 26.sp
                        consoleAdd("Изменение шрифта")
                        shared.edit().putString("size", "26").apply()

                    }, modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = 1.dp)
                )
                {
                    Text("26", fontSize = buttonFontSize, color = if(console_text.value == 26.sp) Color.LightGray else  Color.Black)
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier =  Modifier.padding(start = 20.dp)
                //modifier = Modifier.background(Color.Red)
            ) {


                Checkbox(
                    checked = isCheckedUseLiteralEnter.value,
                    onCheckedChange = {
                        isCheckedUseLiteralEnter.value = it
                        shared.edit().putBoolean("enter", it).apply()
                    },
                    colors = CheckboxDefaults.colors(uncheckedColor = Color.LightGray)
                )


                Text(text = "Вывести символ \\n", color = Color.LightGray)
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier =  Modifier.padding(start = 20.dp)
                //modifier = Modifier.background(Color.Red)
            ) {
                Checkbox(
                    checked = isCheckedUselineVisible.value,
                    onCheckedChange = {
                        isCheckedUselineVisible.value = it
                        shared.edit().putBoolean("lineVisible", it).apply()
                    },
                    colors = CheckboxDefaults.colors(uncheckedColor = Color.LightGray)
                )
                Text(text = "Вывести номер строки", color = Color.LightGray)
            }

        }
    }
}

