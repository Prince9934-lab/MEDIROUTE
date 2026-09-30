package com.example.mediroute

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class RecommendationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)

        val emergencyId = intent.getStringExtra("EMERGENCY_ID") ?: "heart_attack"

        findViewById<ImageView>(R.id.ivBack).setOnClickListener { finish() }

        val tvEmergencyName = findViewById<TextView>(R.id.tvEmergencyName)
        val tvNearestTitle = findViewById<TextView>(R.id.tvNearestTitle)
        val tvNearestName = findViewById<TextView>(R.id.tvNearestName)
        val tvBestTitle = findViewById<TextView>(R.id.tvBestTitle)
        val tvBestName = findViewById<TextView>(R.id.tvBestName)
        val ivNearestImage = findViewById<ImageView>(R.id.ivNearestImage)
        val ivBestImage = findViewById<ImageView>(R.id.ivBestImage)
        val btnRequestVehicle = findViewById<MaterialButton>(R.id.btnRequestVehicle)
        val tvAllHospitalsTab = findViewById<TextView>(R.id.tvAllHospitalsTab)

        tvAllHospitalsTab.setOnClickListener {
            val listItems = if (emergencyId == "fire") {
                arrayOf("Local Fire Station #12", "Central City Fire Dept", "Eastside Fire Station #4", "West End Fire Dept")
            } else {
                arrayOf("City Care Hospital", "Metro Heart Institute", "Global Health Center", "Sunrise Medical Clinic")
            }
            
            val title = if (emergencyId == "fire") "All Fire Stations" else "All Hospitals"

            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setItems(listItems) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Close", null)
                .show()
        }

        when (emergencyId) {
            "fire" -> {
                tvEmergencyName.text = "Emergency: Fire"
                tvNearestTitle.text = "Nearest Fire Station\n(For Immediate Response)"
                tvNearestName.text = "Local Fire Station #12"
                tvBestTitle.text = "Main Fire Headquarters\n(For Major Incidents)"
                tvBestName.text = "Central City Fire Dept"
                btnRequestVehicle.text = "Dispatch Fire Engine"
                ivNearestImage.setImageResource(R.drawable.fire_station_1)
                ivBestImage.setImageResource(R.drawable.fire_station_2)
            }
            "blood_bank" -> {
                tvEmergencyName.text = "Emergency: Blood Bank"
                tvNearestTitle.text = "Nearest Blood Bank\n(For Urgent Units)"
                tvNearestName.text = "City Red Cross"
                tvBestTitle.text = "Major Blood Bank\n(Full Inventory)"
                tvBestName.text = "Central Blood Center"
                btnRequestVehicle.text = "Request Blood Transport"
            }
            "heart_attack" -> tvEmergencyName.text = "Emergency: Heart Attack"
            "stroke" -> tvEmergencyName.text = "Emergency: Stroke"
            "maternity" -> tvEmergencyName.text = "Emergency: Maternity / Labor"
            "poisoning" -> tvEmergencyName.text = "Emergency: Poisoning / Overdose"
            else -> tvEmergencyName.text = "Emergency: Other"
        }

        btnRequestVehicle.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("EMERGENCY_ID", emergencyId)
            startActivity(intent)
        }
    }
}
