package com.vefamobil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vefamobil.navigation.VefaNavHost
import com.vefamobil.ui.theme.VefaMobilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VefaMobilTheme {
                VefaNavHost()
            }
        }
    }
}
