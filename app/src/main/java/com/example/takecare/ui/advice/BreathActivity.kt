package com.example.takecare.ui.advice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.example.takecare.R
import kotlinx.android.synthetic.main.activity_breath.*

class BreathActivity : AppCompatActivity() {

    private var breathing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breath)

        val chronometer = breath_inner_circle_counter

        breath_inner_circle.setOnClickListener {
            if(!breathing){
                chronometer.base = SystemClock.elapsedRealtime()
                chronometer.start()
                breathing = true
            }else{
                chronometer.base = SystemClock.elapsedRealtime()
                breathing = false
            }
        }
    }


}
