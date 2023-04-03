package com.example.rttclientm3.screen.lazy

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rttclientm3.R
import com.example.rttclientm3.telnetWarning

@Composable
fun Warning()
{
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    )
    {
        val image: Painter = painterResource(id = R.drawable.error)
        val warning by telnetWarning.observeAsState()
        if (warning == true) {
            Image(
                painter = image,
                contentDescription = "",
                Modifier
                    .size(48.dp)
                    .padding(end = 10.dp)
            )
        }
    }
}