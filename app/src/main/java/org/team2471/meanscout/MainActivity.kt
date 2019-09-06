package org.team2471.meanscout

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {
    private lateinit var teamView: EditText
    private lateinit var matchView: EditText
    private lateinit var noshowView: Button
    private lateinit var confNoshowView: Button
    private lateinit var supTubView: CheckBox
    private lateinit var collBunnyIncView: Button
    private lateinit var collBunnyDecView: Button
    private lateinit var touchTubIncView: Button
    private lateinit var touchTubDecView: Button
    private lateinit var spoiledBunnyView: CheckBox
    private lateinit var collCubeIncView: Button
    private lateinit var collCubeDecView: Button
    private lateinit var penaltyView: RadioGroup
    private lateinit var driveRatingView: RatingBar
    private lateinit var commentView: EditText
    private lateinit var breakdownView: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        teamView = findViewById(R.id.team)
        matchView = findViewById(R.id.match)
        matchView.setText(match.toString())
        noshowView = findViewById(R.id.noshow)
        confNoshowView = findViewById(R.id.confNoshow)
        supTubView = findViewById(R.id.supTub)
        collBunnyIncView = findViewById(R.id.collBunnyInc)
        collBunnyDecView = findViewById(R.id.collBunnyDec)
        touchTubIncView = findViewById(R.id.touchTubInc)
        touchTubDecView = findViewById(R.id.touchTubDec)
        spoiledBunnyView = findViewById(R.id.spoiledBunny)
        collCubeIncView = findViewById(R.id.collCubeInc)
        collCubeDecView = findViewById(R.id.collCubeDec)
        penaltyView = findViewById(R.id.penalty)
        driveRatingView = findViewById(R.id.driveRating)
        commentView = findViewById(R.id.comment)
        breakdownView = findViewById(R.id.breakdown)
    }

    private var match = 1
    private var bunniesCollected = 0
    private var tubsTouched = 0
    private var cubesCollected = 0
    fun crement(view: View) {
        if (view == collBunnyIncView && bunniesCollected < 6) bunniesCollected++
        if (view == touchTubIncView && tubsTouched < 8) tubsTouched++
        if (view == collCubeIncView && cubesCollected < 120) cubesCollected++
        if (view == collBunnyDecView && bunniesCollected > 0) bunniesCollected--
        if (view == touchTubDecView && tubsTouched > 0) tubsTouched--
        if (view == collCubeDecView && cubesCollected > 0) cubesCollected--
        collBunnyIncView.text = bunniesCollected.toString()
        touchTubIncView.text = tubsTouched.toString()
        collCubeIncView.text = cubesCollected.toString()
    }

    fun onNoShow(@Suppress("UNUSED_PARAMETER")view: View) {
        noshowView.visibility = View.INVISIBLE
        confNoshowView.visibility = View.VISIBLE
    }

    fun confNoShow(@Suppress("UNUSED_PARAMETER")view: View) {
        submit(true)
    }

    fun onSubmit(@Suppress("UNUSED_PARAMETER")view: View) {
        submit(false)
    }

    private fun submit(show: Boolean) {
        noshowView.visibility = View.VISIBLE
        confNoshowView.visibility = View.INVISIBLE
        val sb = StringBuilder()
        sb.append(teamView.text).append(",").append(matchView.text)
        val filename = sb.toString()
        sb.append(",").append(show).append(",").append(supTubView.isChecked)
        sb.append(",").append(bunniesCollected).append(",").append(tubsTouched)
        sb.append(",").append(spoiledBunnyView.isChecked).append(",").append(cubesCollected)
        when (penaltyView.checkedRadioButtonId) {
            2131230851 -> sb.append(",none")
            2131230854 -> sb.append(",yellow")
            2131230852 -> sb.append(",red")
        }
        sb.append(",").append(driveRatingView.rating)
        sb.append(",").append(commentView.text).append(",").append(breakdownView.text)
        applicationContext.openFileOutput("$filename.txt", Context.MODE_PRIVATE).use {
            it.write(sb.toString().toByteArray())
        }
        match = try {
            Integer.parseInt(matchView.text.toString()) + 1
        } catch (e: NumberFormatException) {
            1
        }
        matchView.setText(match.toString())
        teamView.setText("")
        supTubView.isChecked = false
        collBunnyIncView.text = "0"
        bunniesCollected = 0
        touchTubIncView.text = "0"
        tubsTouched = 0
        spoiledBunnyView.isChecked = false
        collCubeIncView.text = "0"
        cubesCollected = 0
        penaltyView.check(R.id.penaltyNone)
        driveRatingView.rating = 0.0f
        commentView.setText("")
        breakdownView.setText("")
        Toast.makeText(applicationContext, "Submitted", Toast.LENGTH_SHORT).show()
    }
}
