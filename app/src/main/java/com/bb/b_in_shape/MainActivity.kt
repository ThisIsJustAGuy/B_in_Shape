package com.bb.b_in_shape

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private val PREFS_NAME = "MyPrefs"
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        checkLogin()
    }

    fun navigateToTraining(v: View) {
        val intent = Intent(this, BodypartActivity::class.java)

        startActivity(intent)
    }

    fun navigateToQR(v: View) {
        val intent = Intent(this, QR_scanner::class.java)

        startActivity(intent)
    }

    private fun checkLogin() {
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val login = findViewById<TextView>(R.id.login_tw)
        val logout = findViewById<TextView>(R.id.logout_tw)
        if (isLoggedIn) {
            login.visibility = View.GONE
            logout.visibility = View.VISIBLE
        } else {
            login.visibility = View.VISIBLE
            logout.visibility = View.GONE
        }
        refreshButtons(isLoggedIn)
    }

    private fun refreshButtons(isLoggedIn: Boolean) {
        val QR = findViewById<ImageButton>(R.id.qr_btn)
        val exercise = findViewById<ImageButton>(R.id.training_btn)
        val suggestion = findViewById<ImageButton>(R.id.suggestion_btn)
        if (isLoggedIn) {
            QR.layoutParams.width = dpToPx(136f)
            exercise.layoutParams.width = dpToPx(136f)
            suggestion.visibility = View.VISIBLE
        } else {
            QR.layoutParams.width = dpToPx(205f)
            exercise.layoutParams.width = dpToPx(205f)
            suggestion.visibility = View.GONE
        }
    }

    fun navigateToLogin(v: View) {
        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)
    }

    fun logOut(v: View) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()
        checkLogin()
    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
            .toInt()
    }
}