package com.example.rttclientm3.screen.lazy.bottomNavigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rttclientm3.R
import com.example.rttclientm3.console

@Composable
fun ButtonClear() {
    IconButton(
        modifier = Modifier.size(34.dp),
        colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF505050)),
        onClick = {
            console._messages.value.clear()
            //console.messages.removeAt(console.messages.lastIndex)
            //console.messages = console.messages.toMutableList()
            //console.consoleAdd("...")
            //console.recompose()
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