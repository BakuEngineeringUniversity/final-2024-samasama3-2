package com.example.petpal.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
        val loginRegisterLabel = findViewById<TextView>(R.id.loginRegisterLabel) // Add this

        // Handle "Next" button click
        // Inside UserRegisterActivity
        nextButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val surname = surnameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val phoneNumber = findViewById<EditText>(R.id.phoneNumberEditText).text.toString() // Add phoneNumber
            val address = findViewById<EditText>(R.id.addressEditText).text.toString() // Add address

            if (firstName.isNotBlank() && surname.isNotBlank() && email.isNotBlank() && password.isNotBlank()
                && phoneNumber.isNotBlank() && address.isNotBlank()) {
                // Pass all data to PetRegisterActivity
                val intent = Intent(this, PetRegisterActivity::class.java)
                intent.putExtra("firstName", firstName)
                intent.putExtra("surname", surname)
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                intent.putExtra("phoneNumber", phoneNumber)
                intent.putExtra("address", address)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }


        // Handle "Already have an account? Login" click
        loginRegisterLabel.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java) // Navigate to LoginActivity
            startActivity(intent)
        }
    }
}
