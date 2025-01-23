package com.example.petpal.activities

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.petpal.R
import com.example.petpal.models.PetUpdateModel
import com.example.petpal.viewmodels.PetViewModel

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

        initializeUIComponents()

        petId = intent.getLongExtra("PET_ID", -1L)
        if (petId == -1L) {
            showToast("Invalid Pet ID")
            finish()
            return
        }

        petViewModel = ViewModelProvider(this).get(PetViewModel::class.java)

        setupSpinner() // Populate the spinner with options
        fetchAndDisplayPetDetails()
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

    private fun setupSpinner() {
        // Define the options for the spinner
        val sexes = listOf("MALE", "FEMALE")

        // Set up an ArrayAdapter to populate the spinner
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            sexes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sexSpinner.adapter = adapter
    }

    private fun fetchAndDisplayPetDetails() {
        petViewModel.fetchPetDetails(petId)

        petViewModel.petDetails.observe(this) { pet ->
            pet?.let {
                nameEditText.setText(it.name)
                typeEditText.setText(it.type)
                ageEditText.setText(it.age.toString())

                // Set spinner selection based on the pet's sex
                val position = if (it.sex == "MALE") 0 else 1
                sexSpinner.setSelection(position)
            } ?: showToast("Failed to fetch pet details")
        }

        petViewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                showToast(it)
                Log.e("UpdatePetActivity", "Error fetching pet details: $it")
            }
        }
    }

    private fun setupButtonListeners() {
        okButton.setOnClickListener {
            updatePetDetails()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun updatePetDetails() {
        val name = nameEditText.text.toString().trim()
        val type = typeEditText.text.toString().trim()
        val sex = sexSpinner.selectedItem.toString() // Get selected sex
        val age = ageEditText.text.toString().toIntOrNull()

        if (name.isBlank() || type.isBlank() || age == null) {
            showToast("Please fill out all fields")
            return
        }

        val petUpdateModel = PetUpdateModel(name, type, sex, age)
        petViewModel.updatePetDetails(petId, petUpdateModel)

        petViewModel.updateResponse.observe(this) { success ->
            if (success) {
                showToast("Pet updated successfully")
                finish()
            } else {
                showToast("Failed to update pet")
            }
        }

        petViewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                showToast(it)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
