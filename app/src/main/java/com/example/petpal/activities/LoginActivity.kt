package com.example.petpal.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.petpal.R
import com.example.petpal.dtos.LoginUserDto
import com.example.petpal.utils.TokenManager
import com.example.petpal.viewmodels.AuthViewModel
import android.widget.TextView

class LoginActivity : AppCompatActivity() {

    // ViewModel instance
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // UI elements
        val usernameEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.loginPasswordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerTextView = findViewById<TextView>(R.id.loginRegisterLabel) // Reference the TextView

        // Navigate to UserRegisterActivity on Register TextView click
        registerTextView.setOnClickListener {
            val intent = Intent(this, UserRegisterActivity::class.java) // Correct Intent to UserRegisterActivity
            startActivity(intent)
        }

        // Observe login response
        authViewModel.loginResponse.observe(this, Observer { response ->
            if (response.status == "success") {
                val token = response.data?.get("token") as? String
                if (token != null) {
                    // Save token using TokenManager
                    val tokenManager = TokenManager(this)
                    tokenManager.saveToken(token)

                    // Navigate to MainActivity
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Token not received!", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Show error message
                Toast.makeText(this, response.message ?: "Login failed!", Toast.LENGTH_SHORT).show()
            }
        })

        // Handle login button click
        loginButton.setOnClickListener {
            val email = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotBlank() && password.isNotBlank()) {
                val loginUserDto = LoginUserDto(email, password)
                authViewModel.loginUser(loginUserDto)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
