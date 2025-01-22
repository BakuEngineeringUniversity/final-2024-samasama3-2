package com.example.petpal.activities

import com.example.petpal.R
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OnBoarding0 : AppCompatActivity() {

    private var currentIndex = 0

    private val largeImages = arrayOf(
        R.drawable.big1,
        R.drawable.big2,
        R.drawable.big3
    )

    // Text for headers and subtexts
    private val headers = arrayOf(
        R.string.header_get_inspired,
        R.string.header_feature_friendly,
        R.string.header_pet_memories
    )

    private val subTexts = arrayOf(
        R.string.subtext_get_inspired,
        R.string.subtext_feature_friendly,
        R.string.subtext_pet_memories
    )

    private lateinit var dot1: ImageView
    private lateinit var dot2: ImageView
    private lateinit var dot3: ImageView
    private lateinit var headerTextView: TextView
    private lateinit var subTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding0)

        val largeImageSwitcher = findViewById<ImageSwitcher>(R.id.large_image_switcher)
        headerTextView = findViewById(R.id.header_text)
        subTextView = findViewById(R.id.sub_text)
        val nextButton = findViewById<Button>(R.id.next_button)
        val skipButton = findViewById<Button>(R.id.skip_button)

        dot1 = findViewById(R.id.dot1)
        dot2 = findViewById(R.id.dot2)
        dot3 = findViewById(R.id.dot3)

        setupImageSwitcher(largeImageSwitcher)

        // Initialize with the first page
        largeImageSwitcher.setImageResource(largeImages[currentIndex])
        updateText(currentIndex)
        updateDots(currentIndex)

        nextButton.setOnClickListener {
            if (currentIndex == largeImages.size - 1) {
                // Move to the LoginActivity after the last page
                val intent = Intent(this@OnBoarding0, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Go to the next page
                currentIndex = (currentIndex + 1) % largeImages.size

                largeImageSwitcher.setImageResource(largeImages[currentIndex])
                updateText(currentIndex)
                updateDots(currentIndex)
            }
        }

        skipButton.setOnClickListener {
            // Skip onboarding and move to LoginActivity
            val intent = Intent(this@OnBoarding0, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupImageSwitcher(imageSwitcher: ImageSwitcher) {
        imageSwitcher.setFactory {
            ImageView(this).apply {
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }

        imageSwitcher.inAnimation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        imageSwitcher.outAnimation = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
    }

    private fun updateText(index: Int) {
        // Set the header and subtext from strings.xml
        headerTextView.text = getString(headers[index])
        subTextView.text = getString(subTexts[index])
    }

    private fun updateDots(index: Int) {
        // Update the active/inactive state of dots
        dot1.setImageResource(R.drawable.inactive_dot)
        dot2.setImageResource(R.drawable.inactive_dot)
        dot3.setImageResource(R.drawable.inactive_dot)

        when (index) {
            0 -> dot1.setImageResource(R.drawable.active_dot)
            1 -> dot2.setImageResource(R.drawable.active_dot)
            2 -> dot3.setImageResource(R.drawable.active_dot)
        }
    }
}
