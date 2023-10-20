package com.example.rttclientm3.screen.lazy

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rttclientm3.console
import com.example.rttclientm3.screen.lazy.bottomNavigation.BottomNavigationLazy

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenLazy(navController: NavHostController) {

    Column(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize().weight(1f))
        {
            console.lazy (Modifier.padding(4.dp))
            Warning()
        }
        BottomNavigationLazy(navController)
    }

}