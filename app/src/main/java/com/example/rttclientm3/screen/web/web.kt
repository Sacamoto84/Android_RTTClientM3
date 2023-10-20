package com.example.rttclientm3.screen.web

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rttclientm3.R
import com.example.rttclientm3.ipESP
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.CoroutineScope
import libs.lan.ping


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SetJavaScriptEnabled")
@Composable
fun Web(navController: NavController) {

    val reload = remember { mutableStateOf(false) }

    val ip = "http://" + ipESP.substring(ipESP.lastIndexOf('/') + 1)
    val state = rememberWebViewState(ip)
    println("URL $ip")

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val navigator = WebViewNavigator(coroutineScope)
    val ping = remember { mutableStateOf(ping(ip)) }

    val swipeRefreshState = rememberSwipeRefreshState(false)

    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = swipeRefreshState,
        onRefresh = {
            println("onRefresh")
            ping.value = ping(ip)
            navigator.reload()
        }
    ) {

        Column(
            Modifier
                .fillMaxSize(),
            //.verticalScroll(rememberScrollState())
        )
        {

            if (ping.value)
                WebView(
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f)
                        .border(
                            width = 5.dp,
                            color = Color(0xFF6650a4),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .verticalScroll(rememberScrollState()),
                    navigator = navigator,
                    state = state,
                    captureBackPresses = false,
                    onCreated = { webWiew ->
                        webWiew.settings.javaScriptEnabled = true
                    }
                )
            else
                Text(
                    text = "Отсутствует связь с $ip",
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    textAlign = TextAlign.Center,

                )

            BottomNavigation(navController)

        }


    }

}

@Composable
private fun BottomNavigation(navController: NavController) {

    Box(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center,
    )
    {
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                modifier = Modifier
                    .height(34.dp)
                    .fillMaxWidth()
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF505050)),
                onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(R.drawable.back1),
                    tint = Color.LightGray,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

        }
    }
}