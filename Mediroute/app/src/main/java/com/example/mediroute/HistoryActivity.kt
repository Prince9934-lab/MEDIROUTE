package com.example.mediroute

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        findViewById<ImageView>(R.id.ivBack).setOnClickListener { finish() }
    }
}
