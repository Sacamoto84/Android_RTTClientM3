package com.example.rttclientm3

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rttclientm3.network.bluetoothAdapter
import com.example.rttclientm3.network.bluetoothManager
import com.example.rttclientm3.network.bt
import com.example.rttclientm3.network.btIsReady
import com.example.rttclientm3.network.decoder
import com.example.rttclientm3.screen.info.ScreenInfo
import com.example.rttclientm3.screen.web.Web
import com.example.rttclientm3.screen.lazy.consoleAdd
import com.example.rttclientm3.ui.theme.RTTClientM3Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.web.LoadingState
import libs.KeepScreenOn
import libs.ipToBroadCast
import libs.readIP
import timber.log.Timber
import timber.log.Timber.*


var contex: Context? = null
lateinit var shared: SharedPreferences

lateinit var ipAddress: String



class MainActivity : ComponentActivity() {

    private val vm: VM by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val ini = Initialization(applicationContext)
        ini.init0()

        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        ipAddress = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
        print(ipAddress)

            //Нужно добавить ее в список лази как текущую
            colorline_and_text.add(
                lineTextAndColor(
                    text = "Первый нах",
                    pairList =
                    listOf<pairTextAndColor>(
                        pairTextAndColor( text = " RTT ", colorText = Color(0xFFFFAA00), colorBg = Color(0xFF812C12)),
                        pairTextAndColor( text = " Terminal ", colorText = Color(0xFFC6D501), colorBg = Color(0xFF587C2F)),
                        pairTextAndColor( text = " v2.7.1 ", colorText = Color(0xFF00E2FF), colorBg = Color(0xFF334292)),
                        pairTextAndColor( text = ">", colorText = Color(0), colorBg = Color(0xFFFF0000)),
                        pairTextAndColor( text = "!", colorText = Color(0), colorBg = Color(0xFFFFCC00)),
                        pairTextAndColor( text = ">", colorText = Color(0), colorBg = Color(0xFF339900)),
                        pairTextAndColor( text = ">", colorText = Color(0), colorBg = Color(0xFF0033CC), flash = true ),
                    )
                )
            )

            consoleAdd("") //Пустая строка


        ini.isInitialised = true

        setContent {

            ipBroadcast = ipToBroadCast(readIP(applicationContext))
            KeepScreenOn()
            vm.launchUIChanelRecive()
            val navController = rememberNavController()
            RTTClientM3Theme {

                val bluetoothPermissions =
                    // Checks if the device has Android 12 or above
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        rememberMultiplePermissionsState(
                            permissions = listOf(
                                android.Manifest.permission.BLUETOOTH,
                                android.Manifest.permission.BLUETOOTH_ADMIN,
                                android.Manifest.permission.BLUETOOTH_CONNECT,
                                android.Manifest.permission.BLUETOOTH_SCAN,
                            )
                        )
                    } else {
                        rememberMultiplePermissionsState(
                            permissions = listOf(
                                android.Manifest.permission.BLUETOOTH,
                                android.Manifest.permission.BLUETOOTH_ADMIN,
                            )
                        )
                    }

                if (bluetoothPermissions.allPermissionsGranted) {
                    btIsReady
                    if (bluetoothAdapter.isEnabled) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            BuildNavGraph(navController)
                        }
                    } else {
                        ButtonBluetooth()
                    }
                }


            }
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ButtonBluetooth() {
    val enableBluetoothContract = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            Log.d("bluetoothLauncher", "Success")
            btIsReady = true
            //bluetoothPrint.print()
        } else {
            Log.w("bluetoothLauncher", "Failed")
            btIsReady = false
        }
    }

    // This intent will open the enable bluetooth dialog
    val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)


    Box(modifier = Modifier.fillMaxSize(), Alignment.Center)
    {
        Button(
            onClick = {
                if (!bluetoothAdapter.isEnabled) {
                    // Bluetooth is off, ask user to turn it on
                    enableBluetoothContract.launch(enableBluetoothIntent)
                }
            }) {
            Text(text = "Включить Bluetooth")
        }
    }


}


@Composable
fun home(navController: NavHostController) {

    com.example.rttclientm3.screen.lazy.lazy(navController, colorline_and_text)

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
            com.example.rttclientm3.screen.lazy.lazy(navController, colorline_and_text)
        }

        composable(route = "info") {
            ScreenInfo(navController)
        }

        composable(route = "web") {
            Web(navController)
        }

    }
}



