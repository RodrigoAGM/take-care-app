package com.example.takecare.ui.advice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.VideoView
import com.example.takecare.R
import kotlinx.android.synthetic.main.activity_breath.*

class BreathActivity : AppCompatActivity() {

    //private var breathing = false
    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breath)

        videoView = breath_video
        /*val chronometer = breath_inner_circle_counter

        breath_inner_circle.setOnClickListener {
            if(!breathing){
                chronometer.base = SystemClock.elapsedRealtime()
                chronometer.start()
                breathing = true
                breath_inner_circle_text.text = getString(R.string.breath_circle_text_breathing)
            }else{
                chronometer.stop()
                chronometer.base = SystemClock.elapsedRealtime()
                breathing = false
                breath_inner_circle_text.text = getString(R.string.breath_circle_text)
            }
        }*/
        configureVideo()

        videoView.setOnClickListener {
            if(videoView.isPlaying){
                videoView.pause()
            }else{
                videoView.start()
            }
        }

        videoView.setOnPreparedListener {
            it.isLooping = true
            breath_progressBar.visibility = View.INVISIBLE
        }
    }

    private fun configureVideo(){
        videoView.setVideoPath(getString(R.string.breath_video_url))
        videoView.seekTo(1)
    }

}
