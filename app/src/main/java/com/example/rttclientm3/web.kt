package com.example.rttclientm3

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.CoroutineScope
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

fun ping(ip: String = "http://192.168.0.200"): Boolean {
    try {
        val url = URL("${ip}")
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("Connection", "close")
        connection.connectTimeout = 1000
        connection.connect()

        return when (connection.responseCode) {
            200, 403 -> true
            else -> false
        }

    } catch (e: Exception) {
        when (e) {
            is MalformedURLException -> "loadLink: Invalid URL ${e.message}"
            is IOException -> "loadLink: IO Exception reading data: ${e.message}"
            is SecurityException -> {
                e.printStackTrace()
                "loadLink: Security Exception. Needs permission? ${e.message}"
            }
            else -> "Unknown error: ${e.message}"
        }
    }
    return false
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Web(navController: NavController) {

    val reload = remember { mutableStateOf(false) }

    val ip = "http://"+ipESP.substring(ipESP.lastIndexOf('/') + 1)
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


    /*

        Box() {

            val reload = remember { mutableStateOf(false) }
            val state = rememberWebViewState("http://$ipESP")
            val coroutineScope: CoroutineScope = rememberCoroutineScope()
            val navigator = WebViewNavigator(coroutineScope)
            val ping = remember { mutableStateOf(ping()) }

            Column(
                Modifier
                    .padding(5.dp)
                    .fillMaxSize()
            )
            {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        reload.value = true
                        ping.value = ping()
                        navigator.reload()
                    })
                {
                    Text(text = "Обновить портал")
                }

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
                    Text(text = "Отсуствует связь с $ipESP")
            }
        }
*/
}
