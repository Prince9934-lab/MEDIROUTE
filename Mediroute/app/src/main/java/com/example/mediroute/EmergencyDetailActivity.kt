package com.example.mediroute

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mediroute.databinding.ActivityEmergencyDetailBinding
import java.util.Locale

class EmergencyDetailActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityEmergencyDetailBinding
    private lateinit var tts: TextToSpeech
    private var isTtsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tts = TextToSpeech(this, this)



        val emergencyId = intent.getStringExtra("EMERGENCY_ID") ?: "heart_attack"
        setupEmergencyDetails(emergencyId)

        // 🚑 Call Ambulance (SAFE VERSION)
        binding.btnCallAmbulance.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:108")
            startActivity(intent)
        }

        // 🗑️ Removed unimplemented tools from this minimalist dialog


        // 🗺️ Open Maps
        binding.btnNearbyHospitals.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        // 🔊 TTS Button
        binding.fabTTS.setOnClickListener {
            if (isTtsReady) {
                val text = "Stay calm. Help is on the way. " +
                        binding.tvFirstAidSteps.text.toString()
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    private fun setupEmergencyDetails(id: String) {
        val titleRes = resources.getIdentifier(id, "string", packageName)
        val instrRes = resources.getIdentifier("ins_$id", "string", packageName)

        if (titleRes != 0) binding.tvEmergencyTitle.text = getString(titleRes)
        if (instrRes != 0) binding.tvFirstAidSteps.text = getString(instrRes)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            if (result != TextToSpeech.LANG_MISSING_DATA &&
                result != TextToSpeech.LANG_NOT_SUPPORTED) {
                isTtsReady = true
            }
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}