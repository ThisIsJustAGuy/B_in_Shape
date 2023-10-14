package com.bb.b_in_shape

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class BodypartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContentView(R.layout.activity_bodypart)

            val btn1 = findViewById<Button>(R.id.Bodypart_btn1)
            btn1.setOnClickListener { _ -> navigateForward(btn1) }

            var back_btn = findViewById<ImageButton>(R.id.Bodypart_back)
            back_btn.setOnClickListener { _ -> navigateBack() }
        }
    }

    fun navigateForward(btn1: Button) {
        val intent = Intent(this, TimeActivity::class.java)
        intent.putExtra("bodypart", btn1.text.toString())

        startActivity(intent)
    }

    fun navigateBack() {
        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)
    }
}