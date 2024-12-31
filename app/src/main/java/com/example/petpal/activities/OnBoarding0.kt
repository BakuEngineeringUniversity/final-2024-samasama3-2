package com.example.petpal.activities


import com.example.petpal.R
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageSwitcher
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class OnBoarding0 : AppCompatActivity() {

    private var currentIndex = 0

    private val largeImages = arrayOf(
        R.drawable.big1,
        R.drawable.big2,
        R.drawable.big3
    )

    private val smallImages = arrayOf(
        R.drawable.small1,
        R.drawable.small2,
        R.drawable.small3
    )

    private lateinit var dot1: ImageView
    private lateinit var dot2: ImageView
    private lateinit var dot3: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding0)

        val largeImageSwitcher = findViewById<ImageSwitcher>(R.id.large_image_switcher)
        val smallImageSwitcher = findViewById<ImageSwitcher>(R.id.small_image_switcher)
        val nextButton = findViewById<Button>(R.id.next_button)
        val skipButton = findViewById<Button>(R.id.skip_button)

        dot1 = findViewById(R.id.dot1)
        dot2 = findViewById(R.id.dot2)
        dot3 = findViewById(R.id.dot3)

        setupImageSwitcher(largeImageSwitcher)
        setupImageSwitcher(smallImageSwitcher)

        largeImageSwitcher.setImageResource(largeImages[currentIndex])
        smallImageSwitcher.setImageResource(smallImages[currentIndex])

        updateDots(currentIndex)

        nextButton.setOnClickListener {
            if (currentIndex == largeImages.size - 1) {
                val intent = Intent(this@OnBoarding0, LoginActivity ::class.java)
                startActivity(intent)
                finish()
            } else {
                currentIndex = (currentIndex + 1) % largeImages.size

                largeImageSwitcher.setImageResource(largeImages[currentIndex])
                smallImageSwitcher.setImageResource(smallImages[currentIndex])

                updateDots(currentIndex)
            }
        }

        skipButton.setOnClickListener {
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

    private fun updateDots(index: Int) {
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