package com.bb.b_in_shape

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ExerciseActivity : ComponentActivity() {

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContentView(R.layout.activity_exercise)

            var back_btn = findViewById<ImageButton>(R.id.Exercise_back)
            back_btn.setOnClickListener { _ -> navigateBack() }

            //val intent = intent
            //val stat = "${intent.getStringExtra("bodypart")} (${intent.getStringExtra("time")})"

            /*val temp="stat"

            val statusText = findViewById<TextView>(R.id.Status_tw)
            statusText.text = temp*/


        }

    }

    fun navigateBack() {
        val intent = Intent(this, TimeActivity::class.java)

        startActivity(intent)
    }
}