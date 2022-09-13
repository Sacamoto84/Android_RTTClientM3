package com.example.rttclientm3

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.VectorDrawable
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rttclientm3.ui.theme.RTTClientM3Theme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import libs.KeepScreenOn
import libs.ipToBroadCast
import libs.readIP

var contex: Context? = null
lateinit var shared: SharedPreferences

lateinit var ipAddress: String

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contex = applicationContext

        shared = getSharedPreferences("size", Context.MODE_PRIVATE)
        console_text.value = shared.getString("size", "12")?.toInt()?.sp ?: 12.sp

        //MARK: Вывод символа эннтер
        isCheckedUseLiteralEnter.value = shared.getBoolean("enter", false)

        //MARK: Вывод номера строки
        isCheckedUselineVisible.value = shared.getBoolean("lineVisible", true)

        //Создаем список цветов из Json цветов
        colorJsonToList()

        val vm: VM by viewModels()
        vm.launchUDPRecive()

        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        ipAddress = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
        print(ipAddress)

        //Нужно добавить ее в список лази как текущую
        colorline_and_text.add(
            lineTextAndColor(
                text = "Первый нах",
                pairList =
                listOf<pairTextAndColor>(
                    pairTextAndColor(
                        text = " RTT ",
                        colorText = Color(0xFFFFAA00),
                        colorBg = Color(0xFF812C12),
                    ),
                    pairTextAndColor(
                        text = " Terminal ",
                        colorText = Color(0xFFC6D501),
                        colorBg = Color(0xFF587C2F),
                    ),
                    pairTextAndColor(
                        text = " v24 ",
                        colorText = Color(0xFF00E2FF),
                        colorBg = Color(0xFF334292),
                    ),
                    pairTextAndColor(
                        text = ">",
                        colorText = Color(0),
                        colorBg = Color(0xFFFF0000),
                    ),
                    pairTextAndColor(
                        text = "!",
                        colorText = Color(0),
                        colorBg = Color(0xFFFFCC00),

                        ),
                    pairTextAndColor(
                        text = ">",
                        colorText = Color(0),
                        colorBg = Color(0xFF339900),

                        ),
                    pairTextAndColor(
                        text = ">",
                        colorText = Color(0),
                        colorBg = Color(0xFF0033CC),
                        flash = true
                    ),
                )
            )
        )
        consoleAdd(" ") //Пустая строка
        setContent {
            ipBroadcast = ipToBroadCast(readIP(applicationContext))
            KeepScreenOn()
            vm.launchUIChanelRecive()
            val navController = rememberNavController()
            RTTClientM3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BuildNavGraph(navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun home(navController: NavHostController)
{
    val pagerState = rememberPagerState()
    HorizontalPager(count = 2, state = pagerState, itemSpacing = 0.dp) { page ->
        when (page) {
            0 -> lazy(navController, colorline_and_text)
            1 -> info(navController)
        }
    }
}

@Composable
fun BuildNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable(route = "home") {
            home(navController)
        }

        composable(route = "console") {
            lazy(navController, colorline_and_text)
        }

        composable(route = "info") {
            info(navController)
        }

        composable(route = "web") {
            Web(navController)
        }

    }
}



