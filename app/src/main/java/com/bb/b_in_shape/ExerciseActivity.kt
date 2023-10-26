package com.bb.b_in_shape

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
class ExerciseActivity : ComponentActivity() {

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)


         //val intent = intent
         //val stat = "${intent.getStringExtra("bodypart")} (${intent.getStringExtra("time")})"

         val temp="stat"

         val statusText = findViewById<TextView>(R.id.Status_tw)
         statusText.text = temp
    }
}