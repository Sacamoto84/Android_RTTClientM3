package com.example.rttclientm3

import android.content.Context
import android.content.SharedPreferences
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.rttclientm3.ui.theme.RTTClientM3Theme
import libs.KeepScreenOn
import libs.ipToBroadCast
import libs.readIP

var contex: Context? = null
lateinit var shared: SharedPreferences

class MainActivity : ComponentActivity() {
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

        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE)  as WifiManager
        val ipAddress: String = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
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
                        text = " v23 ",
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
                    )
                    ,
                    pairTextAndColor(
                        text = ipAddress,
                        colorText = Color(0xFF2196F3),
                        colorBg = Color(0xFF000000),
                        flash = false
                    )
                )
            )
        )
        consoleAdd(" ") //Пустая строка
        setContent {

            ipBroadcast = ipToBroadCast(readIP(applicationContext))
            KeepScreenOn()
            vm.launchUIChanelRecive()

            RTTClientM3Theme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    lazy(colorline_and_text)

                    //info()


                    //HorizontalPager(count = 4, state = pagerState, itemSpacing = 0.dp) { page ->
                    //    when (page) {
                    //        0 -> lazy(colorline_and_text)
                    //        1 -> info()
                    //        2 -> info()
                    //        3 -> Web()
                    //    }
                    // }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RTTClientM3Theme {
        //Greeting("Android")
        Button(onClick = { /*TODO*/ }) {
            Text("sdsdsd")
        }
    }
}