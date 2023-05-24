package com.example.rttclientm3.screen.lazy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.navigation.NavController
import com.example.rttclientm3.R
import com.example.rttclientm3.console
import com.example.rttclientm3.telnetSlegenie
import libs.lan.ipToBroadCast
import libs.lan.readLocalIP
import libs.lan.sendUDP

val colorBg = Color(0xFF1B1B1B)

@Composable
fun BottomNavigationLazy(navController: NavController) {

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
            telnetSlegenie.value = !telnetSlegenie.value!!
            console.lastCount = console.messages.size
        }
    ) {
        console.recompose()
        Text(text = "${ console.messages.size }")
    }
}

@Composable
private fun ButtonClear() {
    IconButton(
        modifier = Modifier.size(34.dp),
        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF505050)),
        onClick = {
            console.messages.clear()
            console.consoleAdd(" ")
            console.recompose()
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
                sendUDP("Reset", ip = ipToBroadCast(readLocalIP(context)), port = 8889)
            if (s == "OK") {
               console.consoleAdd("Команда перезагрузки контроллера")
               console.consoleAdd(" ")
            } else {
                if (s == "sendto failed: ENETUNREACH (Network is unreachable)")
                    console.consoleAdd("Отсуствует Wifi сеть", color = Color.Red)
                else
                    console.consoleAdd(s, color = Color.Red)
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
private fun ButtonSetting(navController: NavController) {
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

