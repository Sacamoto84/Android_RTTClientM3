package com.example.rttclientm3.screen.lazy.bottomNavigation

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rttclientm3.console
import com.example.rttclientm3.telnetSlegenie

@Composable
fun ButtonSlegenie(modifier: Modifier = Modifier)
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
            console.lastCount = console._messages.size
            console.tracking = telnetSlegenie.value!!
        }
    ) {
        //console.recompose()
        //Text(text = "${ console.messages.value.size }")
    }
}