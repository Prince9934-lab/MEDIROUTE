package com.example.mediroute

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class TreatmentPlanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_plan)

        findViewById<ImageView>(R.id.ivBack).setOnClickListener { finish() }

        // Click on the safe card to transition to summary
        findViewById<MaterialCardView>(R.id.cardSafe).setOnClickListener {
            startActivity(Intent(this, SummaryActivity::class.java))
        }
    }
}
