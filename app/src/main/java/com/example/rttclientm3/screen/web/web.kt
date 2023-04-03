package com.example.rttclientm3.screen.web

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rttclientm3.ipESP
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.CoroutineScope
import libs.lan.libLanPing


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SetJavaScriptEnabled")
@Composable
fun Web(navController: NavController) {

    val reload = remember { mutableStateOf(false) }

    val ip = "http://"+ ipESP.substring(ipESP.lastIndexOf('/') + 1)
    val state = rememberWebViewState(ip)
    println("URL $ip")

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val navigator = WebViewNavigator(coroutineScope)
    val ping = remember { mutableStateOf(libLanPing(ip)) }

    val swipeRefreshState = rememberSwipeRefreshState(false)

    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = swipeRefreshState,
        onRefresh = {
            println("onRefresh")
            ping.value = libLanPing(ip)
            navigator.reload()
        }
    ) {

        Column(
            Modifier
                .padding(5.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
        {

            if (ping.value)
                WebView(
                    modifier = Modifier
                        .padding(0.dp)
                        .border(
                            width = 5.dp,
                            color = Color(0xFF6650a4),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    navigator = navigator,
                    state = state,
                    captureBackPresses = false,
                    onCreated = { webWiew ->
                        webWiew.settings.javaScriptEnabled = true
                    }
                )
            else
                Text(text = "Отсуствует связь с $ip")

        }

    }

}
