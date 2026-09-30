package com.example.mediroute

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HealthIdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_id)

        findViewById<ImageView>(R.id.ivBack).setOnClickListener { finish() }

        val tvBloodGroup = findViewById<TextView>(R.id.tvBloodGroup)
        val tvEmergencyContact = findViewById<TextView>(R.id.tvEmergencyContact)

        // Load data from Firebase
        FirebaseHelper.getUserProfile(
            onSuccess = { _, bg, ec ->
                if (bg.isNotEmpty()) tvBloodGroup.text = bg
                if (ec.isNotEmpty()) tvEmergencyContact.text = ec
            },
            onError = {
                // handle error
            }
        )
    }
}
