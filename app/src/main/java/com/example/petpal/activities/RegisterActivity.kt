package com.example.petpal.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petpal.R
import com.example.petpal.dtos.RegisterUserDto
import com.example.petpal.services.AuthService

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val fullNameEditText = findViewById<EditText>(R.id.firstNameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.registerPasswordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)

        val authService = AuthService()

        registerButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (fullName.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                val names = fullName.split(" ")
                val registerUserDto = RegisterUserDto(
                    email = email,
                    firstName = names[0],
                    surname = names.getOrElse(1) { "" },
                    password = password,
                    phoneNumber = "1234567890",
                    address = "123 Address",
                    petName = "Buddy",
                    petSex = "Male",
                    petType = "Dog",
                    petAge = 2
                )
                authService.registerUser(registerUserDto) { message, error ->
                    if (message != null) {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
