package com.example.mediroute

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mediroute.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Toolbar setup
        binding.ivBack.setOnClickListener { finish() }

        // Load existing data
        loadProfileData()

        // ✅ Save Profile
        binding.btnSaveProfile.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val bg = binding.etBloodGroup.text.toString().trim()
            val ec = binding.etEmergencyContact.text.toString().trim()

            if (name.isEmpty() || ec.isEmpty()) {
                Toast.makeText(this, "Name and Contact are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (ec.length < 10) {
                Toast.makeText(this, "Enter valid phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnSaveProfile.isEnabled = false

            FirebaseHelper.saveUserProfile(name, bg, ec) { success ->
                binding.btnSaveProfile.isEnabled = true

                if (success) {
                    Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to save profile", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // ✅ Logout
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun loadProfileData() {
        FirebaseHelper.getUserProfile(
            onSuccess = { name, bg, ec ->
                binding.etName.setText(name)
                binding.etBloodGroup.setText(bg)
                binding.etEmergencyContact.setText(ec)
            },
            onError = {
                // optional error handling
            }
        )
    }
}