package com.example.mediroute

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val ivLogo = findViewById<ImageView>(R.id.ivLogo)
        val tvAppName = findViewById<TextView>(R.id.tvAppName)

        ivLogo.animate().scaleX(1.1f).scaleY(1.1f).setDuration(800).withEndAction {
            ivLogo.animate().scaleX(1f).scaleY(1f).setDuration(800).start()
        }.start()

        tvAppName.alpha = 0f
        tvAppName.animate().alpha(1f).setDuration(1200).start()

        CoroutineScope(Dispatchers.Main).launch {
            delay(2500)
            val auth = FirebaseAuth.getInstance()
            if (auth.currentUser != null) {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
            finish()
        }
    }
}
