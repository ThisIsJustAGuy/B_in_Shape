package com.bb.b_in_shape

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.content.SharedPreferences
import android.widget.CheckBox
import android.view.View
import android.widget.Toast


class ExerciseActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "MyPrefs"

     @SuppressLint("MissingInflatedId")
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContentView(R.layout.activity_exercise)

            val bdp : String = intent.getStringExtra("bodypart").toString()
            val time : String = intent.getStringExtra("time").toString()
            var back_btn = findViewById<ImageButton>(R.id.Exercise_back)
            back_btn.setOnClickListener { _ -> navigateBack(bdp) }

            val intent = intent
            val stat = "${intent.getStringExtra("bodypart")} (${intent.getStringExtra("time")})"

            val statusText = findViewById<TextView>(R.id.Status_tw)
            statusText.text = stat

            val button = findViewById<ImageButton>(R.id.help_btn1)
            button.setOnClickListener {
                val intent = Intent(this, VideoActivity::class.java)
                intent.putExtra("url", "dQw4w9WgXcQ")
                intent.putExtra("bodypart", bdp)
                intent.putExtra("time", time)
                intent.putExtra("exercise", "HELP1")
                startActivity(intent)
            }

            sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

            val chk = findViewById<CheckBox>(R.id.checkBox)
            val chk2 = findViewById<CheckBox>(R.id.checkBox2)
            val chk3 = findViewById<CheckBox>(R.id.checkBox3)
            val chk4 = findViewById<CheckBox>(R.id.checkBox4)

            chk.isChecked = sharedPreferences.getBoolean("chk1_state", false)
            chk2.isChecked = sharedPreferences.getBoolean("chk2_state", false)
            chk3.isChecked = sharedPreferences.getBoolean("chk3_state", false)
            chk4.isChecked = sharedPreferences.getBoolean("chk4_state", false)

            chk.setOnCheckedChangeListener { _, isChecked ->
                val editor = sharedPreferences.edit()
                editor.putBoolean("chk1_state", isChecked)
                editor.apply()
            }

            chk2.setOnCheckedChangeListener { _, isChecked ->
                val editor = sharedPreferences.edit()
                editor.putBoolean("chk2_state", isChecked)
                editor.apply()
            }

            chk3.setOnCheckedChangeListener { _, isChecked ->
                val editor = sharedPreferences.edit()
                editor.putBoolean("chk3_state", isChecked)
                editor.apply()
            }

            chk4.setOnCheckedChangeListener { _, isChecked ->
                val editor = sharedPreferences.edit()
                editor.putBoolean("chk4_state", isChecked)
                editor.apply()
            }

        }

    }

    fun navigateBack(bdp: String) {
        val intent = Intent(this, TimeActivity::class.java)
        intent.putExtra("bodypart", bdp)

        startActivity(intent)
    }
}