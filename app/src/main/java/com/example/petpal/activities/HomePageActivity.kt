package com.example.petpal.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.petpal.R

class HomePageActivity : AppCompatActivity() {

    private lateinit var welcomeMessageTextView: TextView
    private lateinit var editPetButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)

        // Handle edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI components
        welcomeMessageTextView = findViewById(R.id.welcomeMessage)
        editPetButton = findViewById(R.id.editPetButton)

        // Replace [Pet Name] with actual pet name
        val petName = intent.getStringExtra("PET_NAME") ?: "Your Pet" // Fallback to default
        welcomeMessageTextView.text = getString(R.string.welcome_back, petName)

        // Handle edit button click to navigate to UpdatePetActivity
        editPetButton.setOnClickListener {
            val updateIntent = Intent(this, UpdatePetActivity::class.java)
            updateIntent.putExtra("PET_ID", intent.getLongExtra("PET_ID", -1)) // Pass Pet ID
            startActivity(updateIntent)
        }
    }
}
