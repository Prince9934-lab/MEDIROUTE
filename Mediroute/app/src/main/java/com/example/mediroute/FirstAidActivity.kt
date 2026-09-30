package com.example.mediroute

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class FirstAidActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_aid)

        findViewById<ImageView>(R.id.ivBack).setOnClickListener { finish() }

        findViewById<MaterialButton>(R.id.btnNextStep).setOnClickListener {
            startActivity(Intent(this, TreatmentPlanActivity::class.java))
        }
    }
}
