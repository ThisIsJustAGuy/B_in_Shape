package com.bb.b_in_shape

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class BodypartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContentView(R.layout.activity_bodypart)
        }
    }

    fun navigateForward(v: View) {
        val intent = Intent(this, TimeActivity::class.java)
        if (v is Button) {
            intent.putExtra("bodypart", v.text.toString())
            intent.putExtra("bodypart_id", v.contentDescription)
        }

        startActivity(intent)
    }

    fun navigateBack(v: View) {
        finish()
    }
}