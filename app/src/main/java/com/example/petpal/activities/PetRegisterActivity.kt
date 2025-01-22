package com.example.petpal.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petpal.R

class PetRegisterActivity : AppCompatActivity() {

    private lateinit var petSexSpinner: Spinner
    private var selectedPetSex: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_register)

        val petNameEditText = findViewById<EditText>(R.id.petNameEditText)
        val petTypeEditText = findViewById<EditText>(R.id.petTypeEditText)
        val petAgeEditText = findViewById<EditText>(R.id.petAgeEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)

        // Initialize Spinner
        petSexSpinner = findViewById(R.id.petSexSpinner)

        // Populate Spinner
        val petSexOptions = listOf("Select Pet Sex", "Male", "Female")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, petSexOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        petSexSpinner.adapter = adapter

        // Handle Spinner selection
        petSexSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedPetSex = if (position > 0) petSexOptions[position] else null
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedPetSex = null
            }
        }

        // Handle Register Button click
        registerButton.setOnClickListener {
            val petName = petNameEditText.text.toString()
            val petType = petTypeEditText.text.toString()
            val petAge = petAgeEditText.text.toString()

            if (petName.isNotBlank() && petType.isNotBlank() && petAge.isNotBlank() && selectedPetSex != null) {
                Toast.makeText(
                    this,
                    "Pet registered with Name: $petName, Type: $petType, Sex: $selectedPetSex, Age: $petAge",
                    Toast.LENGTH_SHORT
                ).show()

                // Here, you can proceed to save the pet information or pass it to the backend.
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
