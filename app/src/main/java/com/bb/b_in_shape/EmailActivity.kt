package com.bb.b_in_shape

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity


class EmailActivity : ComponentActivity() {
    private lateinit var email_message: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)
    }

    fun sendEmail(v: View) {
        email_message = findViewById<EditText>(R.id.message_et).text.toString()
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf("b.in.shape01@gmail.com"))
        i.putExtra(Intent.EXTRA_SUBJECT, "B In Shape Igénylés")
        i.putExtra(Intent.EXTRA_TEXT, email_message)
        try {
            startActivity(Intent.createChooser(i, getString(R.string.send_email)))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "There are no email clients installed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun navigateHome(v: View) {
        finish()
    }
}