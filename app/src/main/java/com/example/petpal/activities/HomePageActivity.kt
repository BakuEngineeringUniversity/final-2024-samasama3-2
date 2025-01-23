package com.example.petpal.activities

import android.app.AlertDialog
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
    private lateinit var deletePetButton: ImageButton

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
        deletePetButton = findViewById(R.id.deletePetButton)

        // Get the Pet ID from the Intent
        val petId = intent.getLongExtra("PET_ID", -1L)
        Log.d("HomePageActivity", "Received Pet ID: $petId")

        // Check if the Pet ID is valid
        if (petId != -1L) {
            fetchAndDisplayPetDetails(petId)
        } else {
            Log.e("HomePageActivity", "Invalid Pet ID: $petId. Fetching pets for user.")
            fetchAndDisplayFirstPetForUser(11L) // Replace with actual user ID if needed
        }

        // Set up the Edit button click listener
        editPetButton.setOnClickListener {
            Log.d("HomePageActivity", "Edit button clicked. Current Pet ID: $petId")

            // Navigate to UpdatePetActivity with the Pet ID
            val updateIntent = Intent(this, UpdatePetActivity::class.java)
            updateIntent.putExtra("PET_ID", petId)
            startActivity(updateIntent)
        }

        // Set up the Delete button click listener
        deletePetButton.setOnClickListener {
            showDeleteConfirmationDialog(petId)
        }
    }

    /**
     * Show a confirmation dialog before deleting a pet.
     */
    private fun showDeleteConfirmationDialog(petId: Long) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Pet")
        builder.setMessage("Are you sure you want to delete this pet? This action cannot be undone.")

        // Handle "Yes" click
        builder.setPositiveButton("Yes") { dialog, _ ->
            deletePet(petId) // Call delete logic
            dialog.dismiss()
        }

        // Handle "No" click
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss() // Close the dialog
        }

        // Show the dialog
        builder.create().show()
    }

    /**
     * Delete the pet by its ID.
     */
    private fun deletePet(petId: Long) {
        Log.d("HomePageActivity", "Attempting to delete pet with ID: $petId")
        petViewModel.deletePet(petId)

        // Observe the delete result
        petViewModel.deleteResult.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                Log.d("HomePageActivity", "Pet deleted successfully.")
                Toast.makeText(this, "Pet deleted successfully!", Toast.LENGTH_SHORT).show()

                // Optionally, navigate back or refresh the UI
                finish() // Close this activity
            } else {
                Log.e("HomePageActivity", "Failed to delete pet.")
                Toast.makeText(this, "Failed to delete pet. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })

        // Observe errors
        petViewModel.error.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Log.e("HomePageActivity", "Error during deletion: $it")
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                petViewModel.clearError()
            }
        })
    }

    /**
     * Fetch and display details for a specific pet by its ID.
     */
    private fun fetchAndDisplayPetDetails(petId: Long) {
        Log.d("HomePageActivity", "Fetching pet details for Pet ID: $petId")
        petViewModel.fetchPetDetails(petId)

        // Observe the pet details
        petViewModel.petDetails.observe(this, Observer { pet ->
            if (pet != null) {
                Log.d("HomePageActivity", "Pet details fetched successfully: ${pet.name}")
                welcomeMessageTextView.text = getString(R.string.welcome_back, pet.name)
            } else {
                Log.e("HomePageActivity", "Pet details are null or could not be fetched.")
                showToast("Could not fetch pet details. Please try again.")
            }
        })

        // Observe errors
        petViewModel.error.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Log.e("HomePageActivity", "Error fetching pet details: $it")
                showToast(it)
                petViewModel.clearError()
            }
        })
    }

    /**
     * Fetch and display the first pet for a user by their user ID.
     */
    private fun fetchAndDisplayFirstPetForUser(userId: Long) {
        Log.d("HomePageActivity", "Fetching pets for User ID: $userId")
        petViewModel.fetchPetsByUserId(userId)

        // Observe fetched pets
        petViewModel.pets.observe(this, Observer { pets ->
            if (pets != null && pets.isNotEmpty()) {
                val firstPet = pets[0]
                Log.d("HomePageActivity", "First pet fetched successfully: ${firstPet.name}")
                welcomeMessageTextView.text = getString(R.string.welcome_back, firstPet.name)
            } else {
                Log.e("HomePageActivity", "No pets found for the user.")
                showToast("No pets found.")
            }
        })

        // Observe errors
        petViewModel.error.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Log.e("HomePageActivity", "Error fetching pets: $it")
                showToast(it)
                petViewModel.clearError()
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
