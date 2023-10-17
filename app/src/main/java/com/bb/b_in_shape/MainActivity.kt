package com.bb.b_in_shape

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val training_btn = findViewById<ImageButton>(R.id.training_btn)
        training_btn.setOnClickListener { _ -> navigateToTraining() }

        val qr_btn = findViewById<ImageButton>(R.id.qr_btn)
        qr_btn.setOnClickListener { _ -> navigateToQR() }
    }

    fun navigateToTraining() {
        val intent = Intent(this, BodypartActivity::class.java)

        startActivity(intent)
    }

    fun navigateToQR() {
        val intent = Intent(this, QR_Scanner::class.java)

        startActivity(intent)
    }
}