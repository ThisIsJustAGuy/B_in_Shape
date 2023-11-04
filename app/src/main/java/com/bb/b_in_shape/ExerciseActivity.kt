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
import android.widget.Toast
import android.view.Gravity


class ExerciseActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "MyPrefs"
    private var checkboxes: MutableList<CheckBox> = mutableListOf()
    private lateinit var done_bnt: Button
    private lateinit var bdp: String
    private lateinit var time: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContentView(R.layout.activity_exercise)

            bdp = intent.getStringExtra("bodypart").toString()
            time = intent.getStringExtra("time").toString()

            val back_btn = findViewById<ImageButton>(R.id.Exercise_back)
            back_btn.setOnClickListener { _ -> navigateBack(bdp) }

            setStatus()

            done_bnt = findViewById<Button>(R.id.done_btn)
            done_bnt.isEnabled = false
            done_bnt.setOnClickListener {
                val toast = Toast.makeText(this, "Well Done!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 0)
                toast.show()
                resetCheckboxes()
                navigateHome()
            }

            //Help buttons
            helpSetup()

            //Checkboxes
            sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            checkboxSetup()


        }

    }

    private fun setStatus() {
        val statText = "${bdp} > ${time}"
        val statusText = findViewById<TextView>(R.id.Status_tw)
        statusText.text = statText
    }

    private fun helpSetup() {
        val exercise_help = findViewById<ImageButton>(R.id.exercise_help_btn)
        exercise_help.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("url", "dQw4w9WgXcQ")
            intent.putExtra("bodypart", bdp)
            intent.putExtra("time", time)
            intent.putExtra("exercise", "Exercise help")
            startActivity(intent)
        }

        val warmup_help_1 = findViewById<ImageButton>(R.id.warmup1_help_btn)
        warmup_help_1.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("url", "dQw4w9WgXcQ")
            intent.putExtra("bodypart", bdp)
            intent.putExtra("time", time)
            intent.putExtra("exercise", "Warmup help")
            startActivity(intent)
        }

        val warmup_help_2 = findViewById<ImageButton>(R.id.warmup2_help_btn)
        warmup_help_2.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("url", "dQw4w9WgXcQ")
            intent.putExtra("bodypart", bdp)
            intent.putExtra("time", time)
            intent.putExtra("exercise", "Warmup help")
            startActivity(intent)
        }
    }

    private fun checkboxSetup(){
        val ex_chk1 = findViewById<CheckBox>(R.id.set1_cb)
        checkboxes.add(ex_chk1)
        val ex_chk2 = findViewById<CheckBox>(R.id.set2_cb)
        checkboxes.add(ex_chk2)
        val ex_chk3 = findViewById<CheckBox>(R.id.set3_cb)
        checkboxes.add(ex_chk3)
        val ex_chk4 = findViewById<CheckBox>(R.id.set4_cb)
        checkboxes.add(ex_chk4)

        val wm_chk1 = findViewById<CheckBox>(R.id.warmup1_cb)
        checkboxes.add(wm_chk1)
        val wm_chk2 = findViewById<CheckBox>(R.id.warmup2_cb)
        checkboxes.add(wm_chk2)

        val st_chk1 = findViewById<CheckBox>(R.id.stretch1_cb)
        checkboxes.add(st_chk1)
        val st_chk2 = findViewById<CheckBox>(R.id.stretch2_cb)
        checkboxes.add(st_chk2)

        ex_chk1.isChecked = sharedPreferences.getBoolean("ex_chk1_state", false)
        ex_chk2.isChecked = sharedPreferences.getBoolean("ex_chk2_state", false)
        ex_chk3.isChecked = sharedPreferences.getBoolean("ex_chk3_state", false)
        ex_chk4.isChecked = sharedPreferences.getBoolean("ex_chk4_state", false)

        wm_chk1.isChecked = sharedPreferences.getBoolean("wm_chk1_state", false)
        wm_chk2.isChecked = sharedPreferences.getBoolean("wm_chk2_state", false)

        st_chk1.isChecked = sharedPreferences.getBoolean("st_chk1_state", false)
        st_chk2.isChecked = sharedPreferences.getBoolean("st_chk2_state", false)
        done_bnt.isEnabled = allDone()

        ex_chk1.setOnCheckedChangeListener { _, isChecked ->
            checkboxListenerFn(isChecked, "ex_chk1_state")
        }
        ex_chk2.setOnCheckedChangeListener { _, isChecked ->
            checkboxListenerFn(isChecked, "ex_chk2_state")
        }
        ex_chk3.setOnCheckedChangeListener { _, isChecked ->
            checkboxListenerFn(isChecked, "ex_chk3_state")
        }
        ex_chk4.setOnCheckedChangeListener { _, isChecked ->
            checkboxListenerFn(isChecked, "ex_chk4_state")
        }

        wm_chk1.setOnCheckedChangeListener { _, isChecked ->
            checkboxListenerFn(isChecked, "wm_chk1_state")
        }
        wm_chk2.setOnCheckedChangeListener { _, isChecked ->
            checkboxListenerFn(isChecked, "wm_chk2_state")
        }

        st_chk1.setOnCheckedChangeListener { _, isChecked ->
            checkboxListenerFn(isChecked, "st_chk1_state")
        }
        st_chk2.setOnCheckedChangeListener { _, isChecked ->
            checkboxListenerFn(isChecked, "st_chk2_state")
        }
    }

    private fun resetCheckboxes(){
        for (chk in checkboxes) {
            chk.isChecked = false
        }
    }

    private fun allDone(): Boolean {
        for (chk in checkboxes) {
            if (!chk.isChecked) return false
        }
        return true
    }

    private fun checkboxListenerFn(isChecked: Boolean, stateString: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(stateString, isChecked)
        editor.apply()
        done_bnt.isEnabled = allDone()
    }

    private fun navigateBack(bdp: String) {
        val intent = Intent(this, TimeActivity::class.java)
        intent.putExtra("bodypart", bdp)

        startActivity(intent)
    }

    private fun navigateHome(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}