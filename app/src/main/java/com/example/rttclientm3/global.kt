package com.example.rttclientm3

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import com.example.rttclientm3.screen.lazy.Console

/**
 * # Глобальные переменные
 */


/**
 * Флаг того что произошла инициализация
 */
var isInitialized = false


var isCheckedUseLiteralEnter by mutableStateOf(false) //MARK: Показывать в конце строки символ энтер
//val isCheckedUseLineVisible  = mutableStateOf(false) //MARK: Показывать номер строки

/* ----------------------------------------------------------------- */
/**
 * ## Размер текста в консоли
 */
var console_text by mutableStateOf( 12 )
/////////////////////////////////////////////////////////
/**
 * ## Слежение за последней строкой
 */
var telnetSlegenie = MutableLiveData(true)
/////////////////////////////////////////////////////////
/**
 *
 * ## Показ индикатора внимание
 */
var telnetWarning = MutableLiveData(false) //Для отображения значка


var ipBroadcast = "0.0.0.0"

//IP адрес ESP
var ipESP = "0.0.0.0"
var isESPmDNSFinding = false //Признак тог что ip адрес есп найден

val console = Console()








