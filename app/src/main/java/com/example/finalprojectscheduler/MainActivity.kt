package com.example.finalprojectscheduler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.example.finalprojectscheduler.ui.theme.FinalProjectSchedulerTheme
import com.example.finalprojectscheduler.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinalProjectSchedulerTheme {
                NavGraph()
            }
        }
    }
}
