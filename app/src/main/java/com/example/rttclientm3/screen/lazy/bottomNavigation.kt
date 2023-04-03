package com.example.rttclientm3.screen.lazy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rttclientm3.R
import com.example.rttclientm3.colorline_and_text
import com.example.rttclientm3.contex
import com.example.rttclientm3.lastCount
import com.example.rttclientm3.telnetSlegenie
import libs.ipToBroadCast
import libs.lan.readIP
import libs.lan.sendUDP

private val colorBg = Color(0xFF1B1B1B)

@Composable
fun BottomNavigationLazy(navController: NavHostController) {

    Box(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorBg),
        contentAlignment = Alignment.Center,
    )
    {

        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ButtonSlegenie(Modifier.weight(1f))
            //Кнопка сброса списка
            Spacer(modifier = Modifier.width(8.dp))
            ButtonClear() //Кнопка очистка списка
            Spacer(modifier = Modifier.width(16.dp))
            ButtonReset() //Кнопка перегрузки контроллера
            Spacer(modifier = Modifier.width(16.dp))
            ButtonSetting(navController)
            Spacer(modifier = Modifier.width(16.dp))

        }
    }
}


@Composable
private fun ButtonSlegenie(modifier: Modifier = Modifier)
{
    //По кнопке включаем слежение
    val slegenie by telnetSlegenie.observeAsState()

    // Кнопка включения слежения
    Button(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
            .then(modifier),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (slegenie == true) Color(0xFF8AAF4A) else Color.DarkGray
        ),
        onClick = {
            telnetSlegenie.value =
                !telnetSlegenie.value!!
            lastCount = colorline_and_text.size
        }
    ) {

        manual_recomposeLazy.value
        Text(text = "${colorline_and_text.size}")
    }
}

@Composable
private fun ButtonClear() {
    IconButton(
        modifier = Modifier.size(34.dp),
        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF505050)),
        onClick = {
            colorline_and_text.clear()
            consoleAdd(" ")
            manual_recomposeLazy.value = manual_recomposeLazy.value + 1
        }
    )
    {
        Icon(
            painter = painterResource(R.drawable.eraser),
            tint = Color.LightGray,
            contentDescription = null
        )
    }
}

@Composable
private fun ButtonReset() {

    val context = LocalContext.current

    IconButton(
        modifier = Modifier.size(34.dp),
        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF505050)),
        onClick = {
            val s =
                sendUDP("Reset", ip = ipToBroadCast(readIP(context)), port = 8889)
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
        Icon(
            painter = painterResource(R.drawable.reset),
            tint = Color.LightGray,
            contentDescription = null
        )

    }
}

@Composable
private fun ButtonSetting(navController: NavHostController) {
    IconButton(
        modifier = Modifier.size(34.dp),
        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF505050)),
        onClick = {
            navController.navigate("info")
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.settings1),
            tint = Color.LightGray,
            contentDescription = null
        )
    }

}

