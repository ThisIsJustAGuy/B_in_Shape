package com.bb.b_in_shape

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bb.b_in_shape.DATA.dataMatrix


class ExerciseActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "MyPrefs"
    private var checkboxes: MutableList<CheckBox> = mutableListOf()
    private lateinit var done_bnt: Button
    private lateinit var bodypart: String
    private lateinit var time: String
    private lateinit var darkOverlay: View
    private lateinit var popupWindow: PopupWindow
    private lateinit var bodypartid: String
    private lateinit var timeid: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO: itt lekérjük az exercise-okat, és át kell majd passzolni a TextView készítésnek, és a help gombnak
        super.onCreate(savedInstanceState)
        setContent {
            setContentView(R.layout.activity_exercise)

            darkOverlay = findViewById(R.id.darkOverlay)
            showPopupWindow()
            darkOverlay.visibility = View.VISIBLE

            bodypart = intent.getStringExtra("bodypart")!!
            time = intent.getStringExtra("time")!!
            bodypartid = intent.getStringExtra("bodypart_id")!!
            timeid = intent.getStringExtra("time_id")!!


            setStatus()

            done_bnt = findViewById<Button>(R.id.done_btn)
            done_bnt.isEnabled = false
            done_bnt.setOnClickListener {
                val toast = Toast.makeText(this, getString(R.string.well_done), Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 0)
                toast.show()
                resetCheckboxes()
                navigateHome()
            }

            sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

            //Help buttons
            helpSetup()

            //Checkboxes
            checkboxSetup()
        }

    }

    private fun showPopupWindow() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_layout, null)

        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setOnDismissListener {
            darkOverlay.visibility = View.GONE
        }

        val parent = findViewById<ConstraintLayout>(R.id.exercise_whole)
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0)

        val closeButton = popupView.findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    private fun setStatus() {
        val statText = "$bodypart > $time"
        val statusText = findViewById<TextView>(R.id.Status_tw)
        statusText.text = statText
    }

    private fun exerciseCheckboxSetup(amount: Int, exercises: Array<String>) {
        val parentLayout = findViewById<ConstraintLayout>(R.id.exercise_group)
        var prevId = View.NO_ID
        val constraintSet = ConstraintSet()
        constraintSet.clone(parentLayout)


        for (i in 1..amount) {
            var cb = CheckBox(this)
            parentLayout.addView(cb)
            val exercise = exercises[i-1]
            cb.id = View.generateViewId()
            cb.text = "$exercise"


            constraintSet.connect(
                cb.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START
            )
            constraintSet.connect(
                cb.id,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END
            )
            if (prevId == View.NO_ID) {
                constraintSet.connect(
                    cb.id,
                    ConstraintSet.TOP,
                    R.id.exercise_tw,
                    ConstraintSet.BOTTOM
                )
            } else {
                constraintSet.connect(cb.id, ConstraintSet.TOP, prevId, ConstraintSet.BOTTOM)
            }

            constraintSet.setMargin(cb.id, ConstraintSet.TOP, dpToPx(15f))
            constraintSet.setMargin(cb.id, ConstraintSet.START, dpToPx(30f))
            constraintSet.setMargin(cb.id, ConstraintSet.END, dpToPx(60f))
            if (i == amount) {
                constraintSet.setMargin(cb.id, ConstraintSet.BOTTOM, dpToPx(30f))
                constraintSet.connect(cb.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            }

            constraintSet.constrainWidth(cb.id, ConstraintSet.MATCH_CONSTRAINT)
            constraintSet.constrainHeight(cb.id, dpToPx(50f))

            cb.setPaddingRelative(dpToPx(10f), 0, 0, 0)
            cb.setBackgroundResource(R.drawable.yellow_btn_background)
            cb.textSize = dpToSp(20f)
            prevId = cb.id

            checkboxes.add(cb)
            cb.isChecked = sharedPreferences.getBoolean("ex_cb_$i-state", false)
            cb.setOnCheckedChangeListener { _, isChecked ->
                checkboxListenerFn(isChecked, "ex_cb_$i-state")
            }

            generateHelp(cb, constraintSet, generateClickListeners(dataMatrix[0]), i-1)
        }

        constraintSet.applyTo(parentLayout)
    }

    private fun generateHelp(checkbox: CheckBox, constraintSet: ConstraintSet,buttonClickListeners: List<View.OnClickListener>,currentIndex: Int): Int {
        val parentLayout = findViewById<ConstraintLayout>(R.id.exercise_group)
        val helpButton = ImageButton(this)
        parentLayout.addView(helpButton)
        helpButton.id = View.generateViewId()
        constraintSet.constrainWidth(helpButton.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.constrainHeight(helpButton.id, ConstraintSet.WRAP_CONTENT)
        helpButton.setBackgroundResource(R.drawable.question_mark)

        constraintSet.connect(helpButton.id, ConstraintSet.TOP, checkbox.id, ConstraintSet.TOP)
        constraintSet.connect(helpButton.id, ConstraintSet.BOTTOM, checkbox.id, ConstraintSet.BOTTOM)
        constraintSet.connect(helpButton.id, ConstraintSet.START, checkbox.id, ConstraintSet.END)

        constraintSet.setMargin(helpButton.id, ConstraintSet.START, dpToPx(10f))

        helpButton.setOnClickListener(buttonClickListeners[currentIndex])

        return helpButton.id

    }

    private fun generateClickListeners (workoutplan: Array<String>): List<View.OnClickListener>{
        var buttonClickListeners: MutableList<View.OnClickListener> = mutableListOf()
        for (i in 1..workoutplan.size){
            val exercise = workoutplan[i-1]
            val buttonClickListener = View.OnClickListener {
                val intent = Intent(this, VideoActivity::class.java)
                intent.putExtra("url", workoutplan[i+3])
                intent.putExtra("bodypart", bodypart)
                intent.putExtra("time", time)
                intent.putExtra("exercise", exercise)
                intent.putExtra("description", workoutplan[i+7])
                startActivity(intent)
            }
            buttonClickListeners.add(buttonClickListener)
        }
        return buttonClickListeners
    }

    fun checkboxSetup() {
        exerciseCheckboxSetup(timeid.toInt()+1, dataMatrix[bodypartid.toInt()-1])
        val wm_chk = findViewById<CheckBox>(R.id.warmup_cb)
        checkboxes.add(wm_chk)

        val st_chk = findViewById<CheckBox>(R.id.stretch_cb)
        checkboxes.add(st_chk)


        wm_chk.isChecked = sharedPreferences.getBoolean("wm_chk_state", false)

        st_chk.isChecked = sharedPreferences.getBoolean("st_chk_state", false)
        done_bnt.isEnabled = allDone()


        wm_chk.setOnCheckedChangeListener { _, isChecked ->
            checkboxListenerFn(isChecked, "wm_chk_state")
        }

        st_chk.setOnCheckedChangeListener { _, isChecked ->
            checkboxListenerFn(isChecked, "st_chk_state")
        }
    }

    private fun helpSetup() {

        val warmup_help = findViewById<ImageButton>(R.id.warmup_help_btn)
        warmup_help.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("url", "-PlfOH9Xbuc")
            intent.putExtra("bodypart", bodypart)
            intent.putExtra("time", time)
            intent.putExtra("exercise", getString(R.string.warmup_help))
            intent.putExtra("description", getString(R.string.warmupstretch))
            startActivity(intent)
        }

        val stretch_help = findViewById<ImageButton>(R.id.stretch_help_btn)
        stretch_help.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("url", "-PlfOH9Xbuc")
            intent.putExtra("bodypart", bodypart)
            intent.putExtra("time", time)
            intent.putExtra("exercise", getString(R.string.stretch_help))
            intent.putExtra("description", getString(R.string.warmupstretch))
            startActivity(intent)
        }
    }


    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
            .toInt()
    }

    private fun dpToSp(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        ) / resources.displayMetrics.scaledDensity
    }

    private fun resetCheckboxes() {
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

    fun navigateBack(v: View) {
        finish()
    }

    private fun navigateHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}