package com.example.mediroute

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object FirebaseHelper {

    private val db = FirebaseDatabase.getInstance().reference

    private fun getUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun saveUserProfile(name: String, bg: String, ec: String, callback: (Boolean) -> Unit) {
        val userId = getUserId()

        if (userId == null) {
            callback(false)
            return
        }

        val data = mapOf(
            "name" to name,
            "bloodGroup" to bg,
            "emergencyContact" to ec
        )

        db.child("users").child(userId).setValue(data)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun getUserProfile(
        onSuccess: (String, String, String) -> Unit,
        onError: () -> Unit
    ) {
        val userId = getUserId()

        if (userId == null) {
            onError()
            return
        }

        db.child("users").child(userId).get()
            .addOnSuccessListener { snapshot ->
                val name = snapshot.child("name").value?.toString() ?: ""
                val bg = snapshot.child("bloodGroup").value?.toString() ?: ""
                val ec = snapshot.child("emergencyContact").value?.toString() ?: ""
                onSuccess(name, bg, ec)
            }
            .addOnFailureListener {
                onError()
            }
    }
}