package com.example.finalprojectscheduler.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finalprojectscheduler.model.ScheduleItem
import com.example.finalprojectscheduler.storage.SharedPrefsHelper

@Composable
fun ScheduleScreen(navController: NavController) {
    val context = LocalContext.current
    val username = SharedPrefsHelper.getCurrentUser(context)
    if (username == null) {
        // if not logged in, go back to login
        navController.navigate("login") {
            popUpTo("schedule") { inclusive = true }
        }
        return
    }

    var schedules by remember {
        mutableStateOf(SharedPrefsHelper.getSchedule(context, username))
    }

    val grouped = schedules.groupBy { it.day }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                navController.navigate("add")
            }) {
                Text("Add")
            }
            Button(onClick = {
                SharedPrefsHelper.logout(context)
                navController.navigate("login") {
                    popUpTo("schedule") { inclusive = true }
                }
            }) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            val weekdays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
            for (day in weekdays) {
                val dayItems = grouped[day] ?: emptyList()
                if (dayItems.isNotEmpty()) {
                    item {
                        Text(day, style = MaterialTheme.typography.titleMedium)
                    }
                    items(dayItems.size) { index ->
                        val item = dayItems[index]
                        Text(
                            "${item.time} - ${item.content}",
                            modifier = Modifier
                                .padding(start = 8.dp, bottom = 8.dp)
                                .clickable {
                                    navController.navigate("edit/${item.day}/$index")
                                }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}
