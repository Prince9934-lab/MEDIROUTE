package com.example.mediroute

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mediroute.databinding.ActivityHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        
        // Let's store the current location so MapActivity can use it if needed,
        // though MapActivity can also just fetch it again or use isMyLocationEnabled.
        var currentLocation: Location? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupButtons()
        setupBottomNavigation()
        checkLocationPermissionAndFetch()
    }

    private fun setupButtons() {
        binding.btnEmergency.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        
        binding.btnCheckHospitals.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnFireEmergency).setOnClickListener {
            val intent = Intent(this, RecommendationsActivity::class.java)
            intent.putExtra("EMERGENCY_ID", "fire")
            startActivity(intent)
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnBloodBank).setOnClickListener {
            startActivity(Intent(this, BloodBankActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
        binding.navHome.setOnClickListener {
            // Already on home
        }
        binding.navHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        binding.navHealthId.setOnClickListener {
            startActivity(Intent(this, HealthIdActivity::class.java))
        }
        binding.navProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun checkLocationPermissionAndFetch() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        
        binding.tvLocation.text = "Locating..."
        
        fetchLocation()
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLocation = location
                    updateLocationText(location)
                } else {
                    binding.tvLocation.text = "Location not found"
                }
            }
            .addOnFailureListener {
                binding.tvLocation.text = "Failed to get location"
            }
    }

    private fun updateLocationText(location: Location) {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val city = address.locality ?: address.subAdminArea ?: "Unknown City"
                val country = address.countryName ?: "Unknown Country"
                binding.tvLocation.text = "$city, $country"
            } else {
                binding.tvLocation.text = "${location.latitude}, ${location.longitude}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            binding.tvLocation.text = "New Delhi, India" // fallback
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                fetchLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                binding.tvLocation.text = "Location Disabled"
            }
        }
    }
}
