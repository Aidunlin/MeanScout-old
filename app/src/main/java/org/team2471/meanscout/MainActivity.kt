package org.team2471.meanscout

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class MainActivity : AppCompatActivity() {

    private lateinit var tView: EditText   // Team
    private lateinit var mView: EditText   // Match
    private lateinit var nsView: Button    // No Show
    private lateinit var cnsView: Button   // Confirm No Show
    private lateinit var stView: CheckBox  // Supported Tub
    private lateinit var biView: Button    // Bunny Increment
    private lateinit var bdView: Button    // Bunny Decrement
    private lateinit var tiView: Button    // Tub Increment
    private lateinit var tdView: Button    // Tub Decrement
    private lateinit var sbView: CheckBox  // Spoiled Bunny
    private lateinit var ciView: Button    // Cube Increment
    private lateinit var cdView: Button    // Cube Decrement
    private lateinit var pView: RadioGroup // Penalty
    private lateinit var dView: RatingBar  // Driving
    private lateinit var cView: EditText   // Comment
    private lateinit var bView: EditText   // Breakdown

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
            )
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1
            )
        }
        setContentView(R.layout.activity_main)
        tView = findViewById(R.id.team)
        mView = findViewById(R.id.match)
        mView.setText(match.toString())
        nsView = findViewById(R.id.noshow)
        cnsView = findViewById(R.id.confNoshow)
        stView = findViewById(R.id.supTub)
        biView = findViewById(R.id.collBunnyInc)
        bdView = findViewById(R.id.collBunnyDec)
        tiView = findViewById(R.id.touchTubInc)
        tdView = findViewById(R.id.touchTubDec)
        sbView = findViewById(R.id.spoiledBunny)
        ciView = findViewById(R.id.collCubeInc)
        cdView = findViewById(R.id.collCubeDec)
        pView = findViewById(R.id.penalty)
        dView = findViewById(R.id.driveRating)
        cView = findViewById(R.id.comment)
        bView = findViewById(R.id.breakdown)
    }

    private var match = 1
    private var bunniesCollected = 0
    private var tubsTouched = 0
    private var cubesCollected = 0

    fun crement(view: View) {
        if (view == biView && bunniesCollected < 6) bunniesCollected++
        if (view == tiView && tubsTouched < 8) tubsTouched++
        if (view == ciView && cubesCollected < 120) cubesCollected++
        if (view == bdView && bunniesCollected > 0) bunniesCollected--
        if (view == tdView && tubsTouched > 0) tubsTouched--
        if (view == cdView && cubesCollected > 0) cubesCollected--
        biView.text = bunniesCollected.toString()
        tiView.text = tubsTouched.toString()
        ciView.text = cubesCollected.toString()
    }

    fun onNoShow(@Suppress("UNUSED_PARAMETER") view: View) {
        cnsView.visibility = View.VISIBLE
    }

    fun confNoShow(@Suppress("UNUSED_PARAMETER") view: View) {
        submit(true)
    }

    fun onSubmit(@Suppress("UNUSED_PARAMETER") view: View) {
        submit(false)
    }

    private fun submit(show: Boolean) {
        cnsView.visibility = View.INVISIBLE

        val sb = StringBuilder()
        sb.append(tView.text, ",", mView.text)
        sb.append(",", show, ",", stView.isChecked)
        sb.append(",", bunniesCollected, ",", tubsTouched)
        sb.append(",", sbView.isChecked, ",", cubesCollected)
        when (pView.indexOfChild(findViewById<RadioButton>(pView.checkedRadioButtonId))) {
            0 -> sb.append(",none")
            1 -> sb.append(",yellow")
            2 -> sb.append(",red")
        }
        sb.append(",", dView.rating)
        sb.append(",", cView.text.replace(Regex(","), ""))
        sb.append(",", bView.text.replace(Regex(","), ""))

        val file = File(getExternalFilesDir(null), "surveys.txt")
        val fw = BufferedWriter(FileWriter(file, true))
        fw.write(sb.toString())
        fw.newLine()
        fw.close()
        MediaScannerConnection.scanFile(
            applicationContext,
            arrayOf(file.absolutePath), arrayOf("text/plain"), null
        )

        match = try {
            Integer.parseInt(mView.text.toString()) + 1
        } catch (e: NumberFormatException) {
            1
        }
        mView.setText(match.toString())
        tView.setText("")
        stView.isChecked = false
        biView.text = "0"
        bunniesCollected = 0
        tiView.text = "0"
        tubsTouched = 0
        sbView.isChecked = false
        ciView.text = "0"
        cubesCollected = 0
        pView.check(R.id.penaltyNone)
        dView.rating = 0.0f
        cView.setText("")
        bView.setText("")

        val submitToast = Toast.makeText(applicationContext, "Submitted", Toast.LENGTH_SHORT)
        submitToast.setGravity(Gravity.TOP, 0, 100)
        submitToast.show()
        tView.requestFocus()
    }
}
