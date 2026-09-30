package com.example.mediroute

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mediroute.databinding.ActivityMainBinding
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectedEmergencyId: String? = null

    data class EmergencyItem(val id: String, val title: String, val subtitle: String, val iconResId: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener { finish() }

        setupList()

        binding.btnNext.setOnClickListener {
            if (selectedEmergencyId != null) {
                val intent = Intent(this, RecommendationsActivity::class.java)
                intent.putExtra("EMERGENCY_ID", selectedEmergencyId)
                startActivity(intent)
            }
        }
    }

    private fun setupList() {
        val emergencies = listOf(
            EmergencyItem("heart_attack", "Heart Attack", "Chest pain, discomfort, unusual sweating", R.drawable.heart_attack_img),
            EmergencyItem("accident_trauma", "Accident / Trauma", "Vehicle accident, injury, bleeding", R.drawable.doctor_team),
            EmergencyItem("stroke", "Stroke", "Sudden weakness, face drooping, confusion", R.drawable.brain_stroke_img),
            EmergencyItem("breathing_problem", "Breathing Problem", "Severe breathlessness, asthma attack", R.drawable.doctor_team),
            EmergencyItem("maternity", "Maternity / Labor", "Pregnancy complication, active labor", R.drawable.maternity_icon),
            EmergencyItem("poisoning", "Poisoning / Overdose", "Toxic ingestion, drug overdose", R.drawable.poison_icon),
            EmergencyItem("other", "Other Emergency", "Other medical emergency", R.drawable.doctor_team)
        )

        val itemViews = mutableListOf<MaterialCardView>()
        val radioButtons = mutableListOf<RadioButton>()

        for (item in emergencies) {
            val itemView = LayoutInflater.from(this).inflate(R.layout.item_emergency_list, binding.llEmergencies, false) as MaterialCardView
            val ivIcon = itemView.findViewById<ImageView>(R.id.ivIcon)
            val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
            val tvSubtitle = itemView.findViewById<TextView>(R.id.tvSubtitle)
            val radioButton = itemView.findViewById<RadioButton>(R.id.radioButton)
            val llContent = itemView.findViewById<LinearLayout>(R.id.llContent)

            ivIcon.setImageResource(item.iconResId)
            tvTitle.text = item.title
            tvSubtitle.text = item.subtitle

            itemViews.add(itemView)
            radioButtons.add(radioButton)

            itemView.setOnClickListener {
                // Clear all selections
                for (i in itemViews.indices) {
                    itemViews[i].strokeColor = Color.parseColor("#E5E7EB") // light_gray
                    itemViews[i].findViewById<LinearLayout>(R.id.llContent).setBackgroundColor(Color.WHITE)
                    radioButtons[i].isChecked = false
                }

                // Select current
                itemView.strokeColor = Color.parseColor("#E02424") // emergency_red
                llContent.setBackgroundColor(Color.parseColor("#FEE2E2")) // very light red
                radioButton.isChecked = true
                selectedEmergencyId = item.id
            }

            binding.llEmergencies.addView(itemView)
        }
    }
}