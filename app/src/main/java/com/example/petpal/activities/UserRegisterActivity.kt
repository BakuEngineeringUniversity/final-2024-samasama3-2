package com.example.petpal.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petpal.R

class UserRegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)

        // UI elements
        val firstNameEditText = findViewById<EditText>(R.id.firstNameEditText)
        val surnameEditText = findViewById<EditText>(R.id.surnameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.registerPasswordEditText)
        val nextButton = findViewById<Button>(R.id.nextButton)

        // Handle "Next" button click
        nextButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val surname = surnameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (firstName.isNotBlank() && surname.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                // Pass data to PetRegisterActivity using Intent
                val intent = Intent(this, PetRegisterActivity::class.java)
                intent.putExtra("firstName", firstName)
                intent.putExtra("surname", surname)
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
