package com.example.rttclientm3

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.CoroutineScope
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

fun ping(ip: String = "192.169.0.200"): Boolean {

    /*
    try {
        val ipProcess: Process = Runtime.getRuntime().exec("/system/bin/ping -c 1 $ip")
        val exitValue = ipProcess.waitFor()
        ipProcess.destroy()
        return exitValue == 0
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
    */

    try {
        val url = URL("http://192.168.0.200")
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








    //return false
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Web(navController: NavController) {

    Scaffold()
    {
        Box() {

            val reload = remember { mutableStateOf(false) }

            val state1 = rememberWebViewState("http://$ipESP")

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
                        modifier = Modifier.padding(0.dp).border(
                            width = 5.dp,
                            color = Color(0xFF6650a4),
                            shape = RoundedCornerShape(20.dp)
                        ),
                        navigator = navigator,
                        state = state1,
                        captureBackPresses = false,
                        onCreated = { webWiew ->
                            webWiew.settings.javaScriptEnabled = true

                        }
                    )
                else
                    Text(text = "Отсуствует связь с $ipESP")
            }
        }
    }
}


/*
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebPageScreen(urlToRender: String) {
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            loadUrl(urlToRender)
        }
    }, update = {
        it.loadUrl(urlToRender)
    })
}
 */