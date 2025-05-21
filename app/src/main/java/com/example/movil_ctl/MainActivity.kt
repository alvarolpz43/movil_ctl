package com.example.movil_ctl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.movil_ctl.navigation.AppNavigation
import com.example.movil_ctl.ui.theme.Movil_ctlTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        setContent {
            Movil_ctlTheme {
                AppNavigation()
            }
        }
    }
}

