package com.example.petpal.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.petpal.R
import com.example.petpal.viewmodels.PetViewModel

class HomePageActivity : AppCompatActivity() {

    private lateinit var welcomeMessageTextView: TextView
    private lateinit var editPetButton: ImageButton

    // Declare the PetViewModel
    private val petViewModel: PetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // Log entry into HomePageActivity
        Log.d("HomePageActivity", "onCreate triggered. HomePageActivity started.")

        // Initialize UI components
        welcomeMessageTextView = findViewById(R.id.welcomeMessage)
        editPetButton = findViewById(R.id.editPetButton)

        // Get the Pet ID from the Intent
        val petId = intent.getLongExtra("PET_ID", -1L)
        Log.d("HomePageActivity", "Received Pet ID: $petId")

        // Check if the Pet ID is valid
        if (petId != -1L) {
            Log.d("HomePageActivity", "Valid Pet ID: $petId. Fetching pet details...")

            // Fetch the pet details using the ViewModel
            petViewModel.fetchPetDetails(petId)

            // Observe the pet details and update the UI
            petViewModel.petDetails.observe(this, Observer { pet ->
                if (pet != null) {
                    Log.d("HomePageActivity", "Pet details fetched successfully: ${pet.name}")
                    welcomeMessageTextView.text = getString(R.string.welcome_back, pet.name)
                } else {
                    Log.e("HomePageActivity", "Pet details are null or could not be fetched.")
                    showToast("Could not fetch pet details. Please try again.")
                }
            })

            // Observe any errors from the ViewModel
            petViewModel.error.observe(this, Observer { errorMessage ->
                errorMessage?.let {
                    Log.e("HomePageActivity", "Error fetching pet details: $it")
                    showToast(it)
                    petViewModel.clearError()
                }
            })

            // Set up the Edit button click listener
            editPetButton.setOnClickListener {
                Log.d("HomePageActivity", "Edit button clicked. Current Pet ID: $petId")

                // Navigate to UpdatePetActivity with the Pet ID
                val updateIntent = Intent(this, UpdatePetActivity::class.java)
                updateIntent.putExtra("PET_ID", petId)
                startActivity(updateIntent)
            }
        } else {
            Log.e("HomePageActivity", "Invalid Pet ID: $petId")
            showToast("Invalid Pet ID. Unable to fetch details.")
        }

        // Fetch pets for the user
        val userId = 11L // Replace with actual user ID if needed
        Log.d("HomePageActivity", "Fetching pets for User ID: $userId")
        petViewModel.fetchPetsByUserId(userId)

        // Observe fetched pets
        petViewModel.pets.observe(this, Observer { pets ->
            pets?.let {
                if (it.isNotEmpty()) {
                    Log.d("HomePageActivity", "Pets fetched successfully. First pet: ${it[0].name}")
                    // Optionally display the first pet's name
                    welcomeMessageTextView.text = getString(R.string.welcome_back, it[0].name)
                } else {
                    Log.e("HomePageActivity", "No pets found for the user.")
                    showToast("No pets found.")
                }
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
