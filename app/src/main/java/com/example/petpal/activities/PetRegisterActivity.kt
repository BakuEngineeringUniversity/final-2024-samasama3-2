package com.example.petpal.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.petpal.R
import com.example.petpal.models.RegisterUserModel
import com.example.petpal.viewmodels.AuthViewModel

class PetRegisterActivity : AppCompatActivity() {

    private lateinit var petSexSpinner: Spinner
    private var selectedPetSex: String? = null
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_register)

        // Retrieve user data from Intent
        val firstName = intent.getStringExtra("firstName").orEmpty()
        val surname = intent.getStringExtra("surname").orEmpty()
        val email = intent.getStringExtra("email").orEmpty()
        val password = intent.getStringExtra("password").orEmpty()
        val phoneNumber = intent.getStringExtra("phoneNumber").orEmpty()
        val address = intent.getStringExtra("address").orEmpty()

        val petNameEditText = findViewById<EditText>(R.id.petNameEditText)
        val petTypeEditText = findViewById<EditText>(R.id.petTypeEditText)
        val petAgeEditText = findViewById<EditText>(R.id.petAgeEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)
        petSexSpinner = findViewById(R.id.petSexSpinner)

        setupPetSexSpinner()

        // Register button click listener
        registerButton.setOnClickListener {
            val petName = petNameEditText.text.toString().trim()
            val petType = petTypeEditText.text.toString().trim()
            val petAge = petAgeEditText.text.toString().toIntOrNull()

            if (validateFields(petName, petType, petAge)) {
                // Combine user and pet data into a single model
                val registerUserModel = RegisterUserModel(
                    email = email,
                    firstName = firstName,
                    surname = surname,
                    password = password,
                    phoneNumber = phoneNumber,
                    address = address,
                    petName = petName,
                    petType = petType,
                    petSex = selectedPetSex.orEmpty(),
                    petAge = petAge!!
                )

                // Call ViewModel to register user and pet
                authViewModel.registerUser(registerUserModel)
                observeRegistrationResponse()
            } else {
                showToast("Please fill in all fields correctly.")
            }
        }
    }

    private fun setupPetSexSpinner() {
        val petSexOptions = listOf("Select Pet Sex", "Male", "Female")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, petSexOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        petSexSpinner.adapter = adapter

        petSexSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedPetSex = when (position) {
                    1 -> "MALE"
                    2 -> "FEMALE"
                    else -> null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedPetSex = null
            }
        }
    }

    private fun observeRegistrationResponse() {
        authViewModel.authResponse.observe(this) { response ->
            if (response.status == "success") {
                showToast("Registration successful!")
                navigateToLogin()
            } else {
                showToast(response.message ?: "An error occurred during registration.")
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close current activity
    }

    private fun validateFields(petName: String, petType: String, petAge: Int?): Boolean {
        return petName.isNotBlank() && petType.isNotBlank() && petAge != null && petAge > 0 && selectedPetSex != null
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
