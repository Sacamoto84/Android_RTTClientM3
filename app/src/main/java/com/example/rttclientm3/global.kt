package com.example.rttclientm3

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.rememberNavController
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.ArrayList

//Настройки программы, чекбоксы в окне

//MARK: Показывать в конце строки символ энтер
val isCheckedUseLiteralEnter = mutableStateOf(false)

//MARK: Показывать номер строки
val isCheckedUselineVisible = mutableStateOf(false)

var console_text = mutableStateOf( 12.sp )

var telnetSlegenie = MutableLiveData<Boolean>(true)
var telnetWarning = MutableLiveData<Boolean>(false) //Для отображения значка
var lastCount = 0 //Запоминаем последнне значение при отколючении слежения

var slegenie: Boolean = true
var ipBroadcast = "0.0.0.0"

//IP адресс ESP
var ipESP = "0.0.0.0"
var isESPmDNSFinding = false //Пирзнак тог что ip адресс есп найден








