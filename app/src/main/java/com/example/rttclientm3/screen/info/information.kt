package com.example.rttclientm3.screen.info

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.rttclientm3.colorIn256
import com.example.rttclientm3.console
import com.example.rttclientm3.console_text
import com.example.rttclientm3.ipAddress
import com.example.rttclientm3.ipESP
import com.example.rttclientm3.isCheckedUseLiteralEnter
import com.example.rttclientm3.isCheckedUseLineVisible
import com.example.rttclientm3.shared

//MARK: Локальные дефайны
val textSize = 12.sp
val fontWeight = FontWeight.Normal
val boxSize = 32.dp


//// Определяем компонент для отрисовки графики с помощью OpenGl
//@Composable
//fun OpenGlView(vectorAsset: VectorAsset){
//    Box(){
//        OpenGLView(vectorAsset=vectorAsset)
//    }
//}


@Composable
fun ScreenInfo(navController: NavController) {

    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF090909))
    ) {

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

            Spacer(modifier = Modifier.height(5.dp))
            
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

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                onClick = { navController.navigate("web") }
            )
            {
                Text("Открыть Портал", fontSize = 20.sp)
            }

            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = Color.Gray)
            )
            {
                Text(text = "IP адрес телефона $ipAddress", color = Color.White, modifier = Modifier.padding(start = 20.dp, top = 5.dp))
                val str = if (ipESP[0]=='/') ipESP.removePrefix("/") else ipESP
                Text(text = "IP адрес esp.local   $str", color = Color.White, modifier = Modifier.padding(start = 20.dp, bottom = 5.dp))
            }

            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = Color.Gray)
            )
            {
                Column()
                {
                    repeat(2)
                    { y ->
                        Row()
                        {
                            repeat(4)
                            { x ->
                                ElevatedButton(
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
                                    colors = ButtonDefaults.elevatedButtonColors(
                                        containerColor =
                                        if (console_text.value == (12 + x * 2 + y * 8).sp)
                                            Color.LightGray else Color.DarkGray
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 2.5.dp, end = 2.5.dp),
                                    onClick = {
                                        val iii = 12 + x * 2 + y * 8
                                        println("Изменение шрифта на $iii")
                                        console_text.value = iii.sp
                                        console.consoleAdd("Изменение шрифта")
                                        shared.edit().putString("size", "$iii").apply()
                                    }
                                )
                                {
                                    val iii = 12 + x * 2 + y * 8
                                    Text(
                                        text = "$iii", color =
                                        if (console_text.value == (12 + x * 2 + y * 8).sp) Color.Black
                                        else Color.LightGray,
                                        fontSize = 20.sp
                                    )


                                }
                            }
                        }
                    }
                }

            }

            val buttonFontSize = 4.sp


            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 100.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = Color.Gray)
            )
            {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 10.dp)
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


                    Text(text = "Вывести символ \\n", color = Color.White)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 10.dp)
                    //modifier = Modifier.background(Color.Red)
                ) {
                    Checkbox(
                        checked = isCheckedUseLineVisible.value,
                        onCheckedChange = {
                            isCheckedUseLineVisible.value = it
                            shared.edit().putBoolean("lineVisible", it).apply()
                        },
                        colors = CheckboxDefaults.colors(uncheckedColor = Color.LightGray)
                    )
                    Text(text = "Вывести номер строки", color = Color.White)
                }

            }


            Spacer(modifier = Modifier.width(50.dp))


        }
    }
}



