package com.example.mediroute

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val emergencyId = intent.getStringExtra("EMERGENCY_ID") ?: "heart_attack"

        val tvTrackingTitle = findViewById<android.widget.TextView>(R.id.tvTrackingTitle)
        val tvStatusText = findViewById<android.widget.TextView>(R.id.tvStatusText)
        val tvVehicleLabel = findViewById<android.widget.TextView>(R.id.tvVehicleLabel)
        val tvDriverRole = findViewById<android.widget.TextView>(R.id.tvDriverRole)
        val tvDriverName = findViewById<android.widget.TextView>(R.id.tvDriverName)

        when (emergencyId) {
            "fire" -> {
                tvTrackingTitle.text = "Fire Engine Tracking"
                tvStatusText.text = "Fire Engine is on the way"
                tvVehicleLabel.text = "Fire Engine"
                tvDriverRole.text = "Fire Fighter"
                tvDriverName.text = "Captain Sharma"
            }
            "blood_bank" -> {
                tvTrackingTitle.text = "Blood Transport Tracking"
                tvStatusText.text = "Blood Transport is on the way"
                tvVehicleLabel.text = "Transport"
                tvDriverRole.text = "Courier"
                tvDriverName.text = "Amit Patel"
            }
            else -> {
                tvTrackingTitle.text = "Ambulance Tracking"
                tvStatusText.text = "Ambulance is on the way"
                tvVehicleLabel.text = "Vehicle"
                tvDriverRole.text = "Driver"
                tvDriverName.text = "Ravi Kumar"
            }
        }

        findViewById<ImageView>(R.id.ivBack).setOnClickListener { finish() }

        findViewById<android.widget.LinearLayout>(R.id.llDriverInfo).setOnClickListener {
            startActivity(Intent(this, DoctorCallActivity::class.java))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            
            // Try to center on the location from HomeActivity if available
            HomeActivity.currentLocation?.let { loc ->
                val userLocation = LatLng(loc.latitude, loc.longitude)
                mMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
            } ?: run {
                // Fallback if location isn't ready
                val fallbackLocation = LatLng(28.6139, 77.2090)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fallbackLocation, 12f))
            }
        } else {
            // Mock Location if permission denied
            val fallbackLocation = LatLng(28.6139, 77.2090)
            mMap.addMarker(MarkerOptions().position(fallbackLocation).title("New Delhi"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fallbackLocation, 15f))
        }
    }
}
