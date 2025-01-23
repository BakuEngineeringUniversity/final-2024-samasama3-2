package com.example.petpal.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petpal.R
import com.example.petpal.api.ApiClient
import com.example.petpal.models.PetUpdateModel
import com.example.petpal.enums.Sex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdatePetActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var typeEditText: EditText
    private lateinit var sexSpinner: Spinner
    private lateinit var ageEditText: EditText
    private lateinit var saveButton: Button

    private var petId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_pet)

        // Initialize UI components
        nameEditText = findViewById(R.id.nameEditText)
        typeEditText = findViewById(R.id.typeEditText)
        sexSpinner = findViewById(R.id.sexSpinner)
        ageEditText = findViewById(R.id.ageEditText)
        saveButton = findViewById(R.id.saveButton)

        // Get the Pet ID from the intent
        petId = intent.getLongExtra("PET_ID", -1)
        if (petId == -1L) {
            Toast.makeText(this, "Invalid Pet ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Fetch the existing pet details and populate the UI
        fetchPetDetails()

        // Handle save button click
        saveButton.setOnClickListener {
            updatePetDetails()
        }
    }

    private fun fetchPetDetails() {
        // Simulate fetching pet details
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = ApiClient.apiService.getPetById(petId)
                if (response.isSuccessful) {
                    val pet = response.body()?.data
                    pet?.let {
                        nameEditText.setText(it.name)
                        typeEditText.setText(it.type)
                        ageEditText.setText(it.age.toString())
                        sexSpinner.setSelection(if (it.sex == "MALE") 0 else 1) // Assuming "MALE" is index 0
                    }
                } else {
                    Toast.makeText(
                        this@UpdatePetActivity,
                        "Failed to fetch pet details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@UpdatePetActivity,
                    "Error fetching pet details: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updatePetDetails() {
        val name = nameEditText.text.toString()
        val type = typeEditText.text.toString()
        val sex = if (sexSpinner.selectedItem.toString() == "MALE") Sex.MALE else Sex.FEMALE
        val age = ageEditText.text.toString().toIntOrNull()

        if (name.isBlank() || type.isBlank() || age == null) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val petUpdateModel = PetUpdateModel(name, type, sex, age)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = ApiClient.apiService.updatePet(petId, petUpdateModel)
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@UpdatePetActivity,
                        "Pet updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish() // Go back to the previous activity
                } else {
                    Toast.makeText(
                        this@UpdatePetActivity,
                        "Failed to update pet",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@UpdatePetActivity,
                    "Error updating pet: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
