package com.example.alugueldecarrosramyres

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.alugueldecarrosramyres.ui.navigation.AppNavGraph
import com.example.alugueldecarrosramyres.ui.theme.AluguelDeCarrosRamyresTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AluguelDeCarrosRamyresTheme {
                AppNavGraph()
            }
        }
    }
}
