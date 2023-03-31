package com.example.rttclientm3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rttclientm3.network.btIsConnected
import com.example.rttclientm3.screen.consoleAdd
import libs.ipToBroadCast
import libs.readIP
import libs.sendUDP

private val heghtHabigation = 50.dp
private val colorBg = Color(0xFF1B1B1B)

@Composable
fun bottomNavigationLazy(navController: NavHostController) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(heghtHabigation)
            .background(colorBg), contentAlignment = Alignment.Center
    )
    {

        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            //По кнопке включаем слежение
            val slegenie by telnetSlegenie.observeAsState()

            // Кнопка включения слежения
            Button(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (slegenie == true) Color(0xFF8AAF4A) else Color.DarkGray
                ),
                onClick = {
                    telnetSlegenie.value =
                        !telnetSlegenie.value!!
                    lastCount = colorline_and_text.size
                }
            ) {
                Text(text = "${colorline_and_text.count{ it.pairList.isNotEmpty() }}")
            }

            //Кнопка сброса списка
            Spacer(modifier = Modifier.width(8.dp))
            Button(

                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF505050)),
                modifier = Modifier.fillMaxHeight().weight(1f).padding(top = 8.dp, bottom = 8.dp),
                onClick = {
                    colorline_and_text.removeRange(0, colorline_and_text.lastIndex)
                    consoleAdd(" ")
                }
            ) {
                Text(
                    text = "Очистка", color = Color.LightGray
                )
            }

            //Кнопка перезагрузки контроллера
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF505050)),
                modifier = Modifier
                    .fillMaxHeight()
                    //.width(110.dp)
                    .weight(1f)
                    .padding(top = 8.dp, bottom = 8.dp),
                onClick = {
                    val s =
                        sendUDP("Reset", ip = ipToBroadCast(readIP(contex = contex!!)), port = 8889)
                    if (s == "OK") {
                        consoleAdd("Команда перезагрузки контроллера")
                        consoleAdd(" ")
                    } else {
                        if (s == "sendto failed: ENETUNREACH (Network is unreachable)")
                            consoleAdd("Отсуствует Wifi сеть", color = Color.Red)
                        else
                            consoleAdd(s, color = Color.Red)
                    }
                }
            ) {
                Text(
                    text = "Сброс", color = Color.LightGray
                )
            }


            //Кнопка
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF505050)),
                modifier = Modifier
                    .fillMaxHeight().background(if(btIsConnected) Color.Blue else Color.Transparent)
                    //.width(110.dp)
                    .weight(1f)
                    .padding(top = 8.dp, bottom = 8.dp),
                onClick = {

                    navController.navigate("info")

                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.three_dots),
                    tint = Color.LightGray,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

        }
    }
}


