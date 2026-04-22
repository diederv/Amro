package com.amro.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.amro.app.navigation.AmroNavHost
import com.amro.feature.settings.LocaleManager
import com.amro.core.designsystem.theme.AmroTheme

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleManager.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AmroTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AmroNavHost(navController = rememberNavController())
                }
            }
        }
    }
}
