package com.example.rttclientm3

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData

//MARK: Показывать в конце строки символ энтер
val isCheckedUseLiteralEnter = mutableStateOf(false)

//MARK: Показывать номер строки
val isCheckedUselineVisible = mutableStateOf(false)

var console_text = mutableStateOf( 12.sp )

var telnetSlegenie = MutableLiveData<Boolean>(true)
var telnetWarning = MutableLiveData<Boolean>(false) //Для отображения значка
var lastCount = 0 //Запоминаем последнее значение при отключении слежения

var slegenie: Boolean = true
var ipBroadcast = "0.0.0.0"

//IP адрес ESP
var ipESP = "0.0.0.0"
var isESPmDNSFinding = false //Признак тог что ip адрес есп найден








