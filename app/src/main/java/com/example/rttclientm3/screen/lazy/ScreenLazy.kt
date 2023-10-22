package com.example.rttclientm3.screen.lazy

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.rttclientm3.console
import com.example.rttclientm3.screen.lazy.bottomNavigation.BottomNavigationLazy

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenLazy(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationLazy(navController) }
    ) {
        Box(Modifier.fillMaxSize().padding(bottom = it.calculateBottomPadding())) {
            console.lazy(
                //Modifier.padding(4.dp).recomposeHighlighter()
            )
            Warning()
        }
    }
}