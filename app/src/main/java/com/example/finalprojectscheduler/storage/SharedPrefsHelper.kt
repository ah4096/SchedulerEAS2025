package com.example.finalprojectscheduler.storage

import android.content.Context
import com.example.finalprojectscheduler.model.ScheduleItem
import com.example.finalprojectscheduler.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPrefsHelper {
    private const val PREFS_NAME = "MyWeekPrefs"
    private const val USERS_KEY = "users"
    private const val CURRENT_USER_KEY = "current_user"
    private const val SCHEDULE_PREFIX = "schedule_"

    private val gson = Gson()

    fun saveUser(context: Context, user: User) {
        val users = getUsers(context).toMutableList()
        users.add(user)
        val json = gson.toJson(users)
        getPrefs(context).edit().putString(USERS_KEY, json).apply()
    }

    fun getUsers(context: Context): List<User> {
        val json = getPrefs(context).getString(USERS_KEY, "[]")
        val type = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(json, type)
    }

    fun findUser(context: Context, username: String, password: String): Boolean {
        return getUsers(context).any { it.username == username && it.password == password }
    }

    fun isUsernameTaken(context: Context, username: String): Boolean {
        return getUsers(context).any { it.username == username }
    }

    fun setCurrentUser(context: Context, username: String) {
        getPrefs(context).edit().putString(CURRENT_USER_KEY, username).apply()
    }

    fun getCurrentUser(context: Context): String? {
        return getPrefs(context).getString(CURRENT_USER_KEY, null)
    }

    fun logout(context: Context) {
        getPrefs(context).edit().remove(CURRENT_USER_KEY).apply()
    }

    fun saveSchedule(context: Context, username: String, scheduleList: List<ScheduleItem>) {
        val json = gson.toJson(scheduleList)
        getPrefs(context).edit().putString("$SCHEDULE_PREFIX$username", json).apply()
    }

    fun getSchedule(context: Context, username: String): List<ScheduleItem> {
        val json = getPrefs(context).getString("$SCHEDULE_PREFIX$username", "[]")
        val type = object : TypeToken<List<ScheduleItem>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}
