@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.finalprojectscheduler.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finalprojectscheduler.model.ScheduleItem
import com.example.finalprojectscheduler.storage.SharedPrefsHelper

@SuppressLint("DefaultLocale")
@Composable
fun EditScheduleScreen(navController: NavController, day: String, index: Int) {
    val context = LocalContext.current
    val username = SharedPrefsHelper.getCurrentUser(context) ?: return

    val schedules = SharedPrefsHelper.getSchedule(context, username).toMutableList()
    val filtered = schedules.filter { it.day == day }

    if (index !in filtered.indices) {
        Toast.makeText(context, "Invalid schedule", Toast.LENGTH_SHORT).show()
        navController.popBackStack()
        return
    }

    val itemToEdit = filtered[index]

    var selectedDay by remember { mutableStateOf(itemToEdit.day) }
    var time by remember { mutableStateOf(itemToEdit.time) }
    var content by remember { mutableStateOf(itemToEdit.content) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(is24Hour = false)

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val hour = timePickerState.hour
                    val minute = timePickerState.minute
                    time = String.format("%02d:%02d", hour, minute)
                    showTimePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Select Time") },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {
        Text("Edit Schedule", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenuBox(selectedDay) { selectedDay = it }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (time.isNotEmpty()) "Time: $time" else "Pick a time",
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { showTimePicker = true },
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (time.isBlank() || content.isBlank()) {
                Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@Button
            }

            val originalIndex = schedules.indexOfFirst {
                it.day == itemToEdit.day && it.time == itemToEdit.time && it.content == itemToEdit.content
            }

            if (originalIndex != -1) {
                schedules[originalIndex] = ScheduleItem(selectedDay, time, content)
                SharedPrefsHelper.saveSchedule(context, username, schedules)
                navController.navigate("schedule") {
                    popUpTo("edit/$day/$index") { inclusive = true }
                }
            }
        }) {
            Text("Save Changes")
        }
    }
}
