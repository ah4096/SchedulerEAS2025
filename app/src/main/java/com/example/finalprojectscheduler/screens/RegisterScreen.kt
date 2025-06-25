package com.example.finalprojectscheduler.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finalprojectscheduler.model.User
import com.example.finalprojectscheduler.storage.SharedPrefsHelper

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (SharedPrefsHelper.isUsernameTaken(context, username)) {
                Toast.makeText(context, "Username already taken", Toast.LENGTH_SHORT).show()
            } else {
                SharedPrefsHelper.saveUser(context, User(username, password))
                Toast.makeText(context, "Registered successfully!", Toast.LENGTH_SHORT).show()
                navController.popBackStack() // Go back to login
            }
        }) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = {
            navController.popBackStack() // back to login
        }) {
            Text("Already have an account? Login")
        }
    }
}
