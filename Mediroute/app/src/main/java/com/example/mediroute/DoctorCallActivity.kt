package com.example.mediroute

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DoctorCallActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_call)

        findViewById<ImageView>(R.id.ivBack).setOnClickListener { finish() }

        // Mock ending call or clicking anywhere to go to first aid
        findViewById<ImageView>(R.id.ivEndCall).setOnClickListener {
            startActivity(Intent(this, FirstAidActivity::class.java))
            finish()
        }
    }
}
