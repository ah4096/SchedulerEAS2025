@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.finalprojectscheduler.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finalprojectscheduler.model.ScheduleItem
import com.example.finalprojectscheduler.storage.SharedPrefsHelper

@Composable
fun AddScheduleScreen(navController: NavController) {
    val context = LocalContext.current
    val username = SharedPrefsHelper.getCurrentUser(context) ?: return

    var day by remember { mutableStateOf("Monday") }
    var time by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showTimePicker) {
        val context = LocalContext.current
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
            title = {
                Text("Select Time")
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(32.dp)) {
        Text("Add Schedule", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenuBox(day) { day = it }

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
        OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Content") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (time.isBlank() || content.isBlank()) {
                Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                return@Button
            }
            val current = SharedPrefsHelper.getSchedule(context, username).toMutableList()
            current.add(ScheduleItem(day, time, content))
            SharedPrefsHelper.saveSchedule(context, username, current)
            navController.navigate("schedule") {
                popUpTo("add") { inclusive = true }
            }
        }) {
            Text("Save")
        }
    }
}

@Composable
fun DropdownMenuBox(selectedDay: String, onDaySelected: (String) -> Unit) {
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedDay,
            onValueChange = {},
            readOnly = true,
            label = { Text("Day") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor() // Required for proper dropdown position
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            days.forEach { day ->
                DropdownMenuItem(
                    text = { Text(day) },
                    onClick = {
                        onDaySelected(day)
                        expanded = false
                    }
                )
            }
        }
    }
}
