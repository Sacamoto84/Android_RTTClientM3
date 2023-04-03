package com.example.rttclientm3

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rttclientm3.network.bluetoothAdapter
import com.example.rttclientm3.network.btIsReady
import com.example.rttclientm3.screen.info.ScreenInfo
import com.example.rttclientm3.screen.web.Web
import com.example.rttclientm3.ui.theme.RTTClientM3Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import libs.KeepScreenOn
import libs.ipToBroadCast
import libs.lan.readIP
import timber.log.Timber.*


var contex: Context? = null
lateinit var shared: SharedPreferences

lateinit var ipAddress: String


const val version = "v2.7.1"


class MainActivity : ComponentActivity() {

    private val vm: VM by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ini = Initialization(applicationContext)
        ini.init0()

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



