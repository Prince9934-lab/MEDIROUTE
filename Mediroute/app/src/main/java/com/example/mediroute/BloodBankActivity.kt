package com.example.mediroute

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class BloodBankActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blood_bank)

        findViewById<ImageView>(R.id.ivBack).setOnClickListener { finish() }

        val spinnerBloodGroup = findViewById<Spinner>(R.id.spinnerBloodGroup)
        val tvStock1 = findViewById<TextView>(R.id.tvStock1)
        val tvStock2 = findViewById<TextView>(R.id.tvStock2)
        val btnRequestTransport = findViewById<MaterialButton>(R.id.btnRequestTransport)

        val bloodGroups = arrayOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bloodGroups)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBloodGroup.adapter = adapter

        spinnerBloodGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Simulate stock changing based on blood group selected
                val selected = bloodGroups[position]
                if (selected == "O-") {
                    tvStock1.text = "1 Unit"
                    tvStock2.text = "0 Units"
                } else if (selected == "AB+") {
                    tvStock1.text = "12 Units"
                    tvStock2.text = "8 Units"
                } else {
                    tvStock1.text = "5 Units"
                    tvStock2.text = "2 Units"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnRequestTransport.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("EMERGENCY_ID", "blood_bank")
            startActivity(intent)
        }
    }
}
