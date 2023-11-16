package com.bb.b_in_shape

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class LoginActivity : ComponentActivity() {
    private lateinit var username_et: EditText
    private lateinit var password_et: EditText
    private lateinit var login_btn: Button
    private val PREFS_NAME = "MyPrefs"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        username_et = findViewById(R.id.username_et)
        password_et = findViewById(R.id.password_et)
        login_btn = findViewById(R.id.login_btn)
    }

    fun navigateHome (v: View) {
        finish()
    }

    fun onLoginButtonClick(v: View) {
        val username = username_et.text.toString()
        val password = password_et.text.toString()

        if (isValidCredentials(username, password)) {
            val editor = sharedPreferences.edit()
            editor.putString("username", username)
            editor.apply()
            finish()
        } else {
            Toast.makeText(this, getString(R.string.invalid_login), Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidCredentials(username: String, password: String): Boolean {
        //TODO: itt kell majd lekérni, hogy valid-e a bejelentkezés
        return username == "user" && password == "password"
    }
}