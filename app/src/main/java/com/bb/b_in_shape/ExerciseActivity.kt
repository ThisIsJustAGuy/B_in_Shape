package com.bb.b_in_shape

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ExerciseActivity : ComponentActivity() {

     @SuppressLint("MissingInflatedId")
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContentView(R.layout.activity_exercise)

            val bdp : String = intent.getStringExtra("bodypart").toString()
            var back_btn = findViewById<ImageButton>(R.id.Exercise_back)
            back_btn.setOnClickListener { _ -> navigateBack(bdp) }

            val intent = intent
            val stat = "${intent.getStringExtra("bodypart")} (${intent.getStringExtra("time")})"

            val statusText = findViewById<TextView>(R.id.Status_tw)
            statusText.text = stat


        }

    }

    fun navigateBack(bdp: String) {
        val intent = Intent(this, TimeActivity::class.java)
        intent.putExtra("bodypart", bdp)

        startActivity(intent)
    }
}