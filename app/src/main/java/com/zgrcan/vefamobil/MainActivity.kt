package com.zgrcan.vefamobil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.zgrcan.vefamobil.navigation.VefaNavHost
import com.zgrcan.vefamobil.ui.theme.VefaMobilTheme

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
