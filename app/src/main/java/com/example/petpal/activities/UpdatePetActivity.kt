package com.example.petpal.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.petpal.R
import com.example.petpal.models.PetUpdateModel
import com.example.petpal.viewmodels.PetViewModel
import com.example.petpal.enums.Sex

class UpdatePetActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var typeEditText: EditText
    private lateinit var sexSpinner: Spinner
    private lateinit var ageEditText: EditText
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button

    private var petId: Long = -1L
    private lateinit var petViewModel: PetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_pet)

        // Initialize UI components
        initializeUIComponents()

        // Get the Pet ID from the Intent
        petId = intent.getLongExtra("PET_ID", -1L)

        // Validate the petId
        if (petId == -1L) {
            showToast("Invalid Pet ID")
            Log.e("UpdatePetActivity", "Pet ID not provided or invalid")
            finish()
            return
        }

        // Additional validation for invalid IDs (e.g., petId == 1L)
        if (petId == 1L) {
            showToast("Invalid Pet ID: $petId")
            Log.e("UpdatePetActivity", "Pet ID is not valid: $petId")
            finish()
            return
        }

        // Initialize ViewModel
        petViewModel = ViewModelProvider(this).get(PetViewModel::class.java)

        // Fetch the existing pet details and populate the UI
        fetchPetDetails()

        // Handle button clicks
        setupButtonListeners()
    }

    private fun initializeUIComponents() {
        nameEditText = findViewById(R.id.nameEditText)
        typeEditText = findViewById(R.id.typeEditText)
        sexSpinner = findViewById(R.id.sexSpinner)
        ageEditText = findViewById(R.id.ageEditText)
        okButton = findViewById(R.id.okButton)
        cancelButton = findViewById(R.id.cancelButton)
    }

    private fun setupButtonListeners() {
        okButton.setOnClickListener {
            updatePetDetails()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun fetchPetDetails() {
        Log.d("UpdatePetActivity", "Fetching details for Pet ID: $petId")
        petViewModel.fetchPetDetails(petId)

        // Observe pet details
        petViewModel.petDetails.observe(this) { pet ->
            pet?.let {
                nameEditText.setText(it.name)
                typeEditText.setText(it.type)
                ageEditText.setText(it.age.toString())
                sexSpinner.setSelection(if (it.sex == Sex.MALE.name) 0 else 1)
            } ?: run {
                showToast("Failed to fetch pet details")
                Log.e("UpdatePetActivity", "Failed to fetch pet details for Pet ID: $petId")
            }
        }

        // Observe errors
        petViewModel.error.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                showToast(errorMessage)
                Log.e("UpdatePetActivity", "Error fetching pet details: $errorMessage")
            }
        }
    }

    private fun updatePetDetails() {
        val name = nameEditText.text.toString().trim()
        val type = typeEditText.text.toString().trim()
        val sex = if (sexSpinner.selectedItem.toString() == "MALE") Sex.MALE else Sex.FEMALE
        val age = ageEditText.text.toString().toIntOrNull()

        if (name.isBlank() || type.isBlank() || age == null) {
            showToast("Please fill out all fields")
            return
        }

        val petUpdateModel = PetUpdateModel(name, type, sex, age)
        petViewModel.updatePetDetails(petId, petUpdateModel)

        // Observe update response
        petViewModel.updateResponse.observe(this) { success ->
            if (success) {
                showToast("Pet updated successfully")
                finish()
            } else {
                showToast("Failed to update pet")
            }
        }

        // Observe errors
        petViewModel.error.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                showToast(errorMessage)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
