package com.bb.b_in_shape

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class TimeActivity : ComponentActivity() {
    lateinit var bodypart: String
    lateinit var bodypartid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        bodypart = intent.getStringExtra("bodypart")!!
        bodypartid = intent.getStringExtra("bodypart_id")!!

        var statusText = findViewById<TextView>(R.id.Status_tw)
        statusText.text = bodypart
    }

    fun navigateBack(v: View) {
        finish()
    }

    fun navigateForward(v: View) {
        val intent = Intent(this, ExerciseActivity::class.java)
        if (v is Button) {
            intent.putExtra("time", v.text)
            intent.putExtra("bodypart", bodypart)
            intent.putExtra("bodypart_id", bodypartid)
            intent.putExtra("time_id", v.contentDescription)
        }
        startActivity(intent)
    }
}