package com.example.taskmanagerapp

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.taskmanagerapp.databinding.ActivitySplashscreenBinding


class SplashscreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashscreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivitySplashscreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Delay and then navigate to the main activity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Prevent going back to the splash screen
        }, 1500)

        createAnimation(binding.splashImg, R.anim.fade)
        createAnimation(binding.textView2, R.anim.fade)
    }

    private fun createAnimation(view: View, animResId: Int) {

            // Load the animation from the specified resource ID
            val animation = AnimationUtils.loadAnimation(this, animResId)
            // Start the animation on the ImageView
            view.startAnimation(animation)
    }

}
