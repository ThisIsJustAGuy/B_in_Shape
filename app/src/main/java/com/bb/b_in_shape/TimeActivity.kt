package com.bb.b_in_shape

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity

class TimeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        val intent = intent
        val bodypart = intent.getStringExtra("bodypart")

        var statusText = findViewById<TextView>(R.id.Status_tw)
        statusText.text = bodypart

        var back_btn = findViewById<ImageButton>(R.id.Time_back)
        back_btn.setOnClickListener { _ -> navigateBack() }

        var time_btn1 = findViewById<Button>(R.id.Time_btn1)
        time_btn1.setOnClickListener { _ -> navigateForward(time_btn1) }
    }

    private fun navigateBack() {
        val intent = Intent(this, BodypartActivity::class.java)

        startActivity(intent)
    }

    private fun navigateForward(time_btn1: Button) {
        val intent = Intent(this, ExerciseActivity::class.java)
        intent.putExtra("time", time_btn1.text.toString())
        startActivity(intent)
    }
}