package com.example.rttclientm3

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import libs.console.Console
import libs.console.LineTextAndColor

val isCheckedUseLiteralEnter = mutableStateOf(false) //MARK: Показывать в конце строки символ энтер
val isCheckedUseLineVisible  = mutableStateOf(false) //MARK: Показывать номер строки

var console_text = mutableStateOf( 12.sp ) //Размер текста в консоли

var telnetSlegenie = MutableLiveData<Boolean>(true)
var telnetWarning = MutableLiveData<Boolean>(false) //Для отображения значка
var lastCount = 0 //Запоминаем последнее значение при отключении слежения

var slegenie: Boolean = true
var ipBroadcast = "0.0.0.0"

//IP адрес ESP
var ipESP = "0.0.0.0"
var isESPmDNSFinding = false //Признак тог что ip адрес есп найден



val colorline_and_text = mutableListOf<LineTextAndColor>()

val console = Console(colorline_and_text)








