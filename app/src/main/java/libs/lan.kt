package libs

import android.content.Context
import android.net.wifi.WifiManager
import android.os.StrictMode
import android.text.format.Formatter
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.*

// ! Добавить в манифест
// <uses-permission android:name= "android.permission.INTERNET" />
// <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
// <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

/*

   * ddddddd
   # dffffff
   ! eee3333

 */

/*

*     ░░░░░░░░░
    * ▒▒▒▒▒▒▒▒▒ opopop ▒▒▒▒▒▒▒▒
    *  ╔═════════╗
    *  ║ sendUDP ║====> ┌─────┐
    *  ╚═════════╝      │  !  │
    *                   └─────┘


*/

//=====================================================
// Отправить Udp сообщение * Возвращает OK или ошибку
// region // sendUDP(messageStr: String, ip :String, port: Int): String
fun sendUDP(messageStr: String, ip: String, port: Int): String {
    // Hack Prevent crash (sending should be done using an async task)
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)
    try {
        val socket = DatagramSocket()
        socket.broadcast = true
        val sendData = messageStr.toByteArray()
        val sendPacket =
            DatagramPacket(sendData, sendData.size, InetAddress.getByName(ip), port)
        socket.send(sendPacket)
        println("sendUDP: $ip:$port")
    } catch (e: IOException) {
        Log.e("sendUDP", "IOException: " + e.message)
        return e.message.toString()
    }
    return "OK"
}
//endregion
//=====================================================

//=====================================================
// val Context = applicationContext
// Получить IP адресс Wifi -> "192.168.0.100"
// region > readIP(contex : Context): String
fun readIP(contex: Context): String {
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    StrictMode.setThreadPolicy(policy)
    try {
        val wifiManager =
            contex.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipAddress: String = Formatter.formatIpAddress(wifiManager.connectionInfo.ipAddress)
        println("IP: $ipAddress")
        return ipAddress
    } catch (e: IOException) {
        Log.e("readIP1", "IOException: " + e.message)
    }
    return "127.0.0.1"
}
//endregion
//=====================================================

//=====================================================
// IP сделать broadcast "192.168.0.100" -> "192.168.0.255"
// region > ipToBroadCast(value : String): String
fun ipToBroadCast(value: String): String {
    val ip = InetAddress.getByName(value)
    val bytes = ip.address
    val ibytes: IntArray = IntArray(4)
    for (i in bytes.indices) {
        if (bytes[i] >= 0) ibytes[i] = bytes[i].toInt()
        else {
            ibytes[i] = 256 + bytes[i].toInt()
        }
    }
    ibytes[3] = 255
    val broadcast = "${ibytes[0]}.${ibytes[1]}.${ibytes[2]}.255"
    println("ipToBroadCast : $broadcast")
    return broadcast
}
//endregion
//=====================================================


fun ping(ip: String = "http://192.168.0.200"): Boolean {
    try {
        val url = URL(ip)
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


fun ping2(ip: String = "192.168.0.200"): Boolean {
    val address = InetAddress.getByName("192.168.0.200")
    return address.isReachable(1000)
}

fun runSystemCommand(command: String?):Boolean {
    try {
        val p = Runtime.getRuntime().exec(command)
        val exitValue: Int = p.waitFor()
        //println("runSystemCommand exitValue ========= $exitValue")
        return (exitValue == 0)
        /*
        val inputStream = BufferedReader( InputStreamReader(p.inputStream)   )
        var s: String? = ""
        while (inputStream.readLine().also { s = it } != null)
        {  println(s)  }
         */

    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        println(e.message)
    }
    return false
}


fun ping3(ip: String = "192.168.0.200"): Boolean {
    return runSystemCommand("/system/bin/ping -c 1 $ip")
}

/*
public boolean isConnectedToThisServer(String host) {
    Runtime runtime = Runtime.getRuntime();
    try {
        Process ipProcess = runtime.exec("/system/bin/ping
                -c 1 8.8.8.8" + host);
        int exitValue = ipProcess.waitFor();
        return (exitValue == 0);
    } catch (IOException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return false;
}
 */