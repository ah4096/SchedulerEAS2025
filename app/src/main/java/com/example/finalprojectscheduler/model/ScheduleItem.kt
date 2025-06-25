package com.example.finalprojectscheduler.model

data class ScheduleItem(
    val day: String,     // e.g. "Monday"
    val time: String,    // e.g. "14:30"
    val content: String  // e.g. "Go to gym"
)