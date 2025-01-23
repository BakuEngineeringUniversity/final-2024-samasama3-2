package com.example.petpal.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.petpal.R
import com.example.petpal.api.ApiClient
import com.example.petpal.api.ApiService
import com.example.petpal.models.RegisterUserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PetRegisterActivity : AppCompatActivity() {

    private lateinit var petSexSpinner: Spinner
    private var selectedPetSex: String? = null
    private val apiService: ApiService by lazy { ApiClient.apiService } // Using global ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_register)

        // Retrieve user data from Intent
        val firstName = intent.getStringExtra("firstName")
        val surname = intent.getStringExtra("surname")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val address = intent.getStringExtra("address")

        val petNameEditText = findViewById<EditText>(R.id.petNameEditText)
        val petTypeEditText = findViewById<EditText>(R.id.petTypeEditText)
        val petAgeEditText = findViewById<EditText>(R.id.petAgeEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)
        petSexSpinner = findViewById(R.id.petSexSpinner)

        // Map pet sex options
        val petSexOptions = mapOf(
            "Select Pet Sex" to null,
            "Male" to "MALE",
            "Female" to "FEMALE"
        )

        // Populate spinner with user-friendly options
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, petSexOptions.keys.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        petSexSpinner.adapter = adapter

        petSexSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLabel = petSexSpinner.selectedItem.toString()
                selectedPetSex = petSexOptions[selectedLabel]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedPetSex = null
            }
        }

        // Register button click listener
        registerButton.setOnClickListener {
            val petName = petNameEditText.text.toString()
            val petType = petTypeEditText.text.toString()
            val petAge = petAgeEditText.text.toString().toIntOrNull() ?: 0

            if (petName.isNotBlank() && petType.isNotBlank() && petAge > 0 && selectedPetSex != null) {
                val registerUserModel = RegisterUserModel(
                    email = email ?: "",
                    firstName = firstName ?: "",
                    surname = surname ?: "",
                    password = password ?: "",
                    phoneNumber = phoneNumber ?: "",
                    address = address ?: "",
                    petName = petName,
                    petType = petType,
                    petSex = selectedPetSex ?: "",
                    petAge = petAge
                )

                // Log the payload being sent
                println("RegisterUserDto: $registerUserModel")

                // Call the API to register the user
                registerUser(registerUserModel)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(registerUserModel: RegisterUserModel) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.registerUser(registerUserModel)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PetRegisterActivity, "Registration successful!", Toast.LENGTH_SHORT).show()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        println("Error response: $errorMessage")
                        Toast.makeText(this@PetRegisterActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Network error: ${e.message}")
                    Toast.makeText(this@PetRegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
