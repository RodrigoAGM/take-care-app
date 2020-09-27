package com.example.takecare.ui.advice

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.takecare.R
import kotlinx.android.synthetic.main.activity_sounds.*

class SoundsActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sounds)
        mediaPlayer = MediaPlayer()

        sound_1.setOnClickListener {
            if(mediaPlayer.isPlaying){
                resetPlayers()
                mediaPlayer.stop()
                mediaPlayer.reset()
            }else{
                sound_1_icon.setImageResource(R.drawable.ic_pause_circle_solid)
                mediaPlayer.setDataSource(getString(R.string.sounds_1_url))
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        }

        sound_2.setOnClickListener {
            if(mediaPlayer.isPlaying){
                resetPlayers()
                mediaPlayer.stop()
                mediaPlayer.reset()
            }else{
                sound_2_icon.setImageResource(R.drawable.ic_pause_circle_solid)
                mediaPlayer.setDataSource(getString(R.string.sounds_2_url))
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        }

        sound_3.setOnClickListener {
            if(mediaPlayer.isPlaying){
                resetPlayers()
                mediaPlayer.stop()
                mediaPlayer.reset()
            }else{
                sound_3_icon.setImageResource(R.drawable.ic_pause_circle_solid)
                mediaPlayer.setDataSource(getString(R.string.sounds_3_url))
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        }

        sound_4.setOnClickListener {
            if(mediaPlayer.isPlaying){
                resetPlayers()
                mediaPlayer.stop()
                mediaPlayer.reset()
            }else{
                sound_4_icon.setImageResource(R.drawable.ic_pause_circle_solid)
                mediaPlayer.setDataSource(getString(R.string.sounds_4_url))
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        }

        sound_5.setOnClickListener {
            if(mediaPlayer.isPlaying){
                resetPlayers()
                mediaPlayer.stop()
                mediaPlayer.reset()
            }else{
                sound_5_icon.setImageResource(R.drawable.ic_pause_circle_solid)
                mediaPlayer.setDataSource(getString(R.string.sounds_5_url))
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        }
    }

    private fun resetPlayers(){
        sound_1_icon.setImageResource(R.drawable.ic_play_circle_solid)
        sound_2_icon.setImageResource(R.drawable.ic_play_circle_solid)
        sound_3_icon.setImageResource(R.drawable.ic_play_circle_solid)
        sound_4_icon.setImageResource(R.drawable.ic_play_circle_solid)
        sound_5_icon.setImageResource(R.drawable.ic_play_circle_solid)
    }

    override fun onStop() {
        super.onStop()
        resetPlayers()
        mediaPlayer.stop()
        mediaPlayer.reset()
    }

    override fun onDestroy() {
        super.onDestroy()
        resetPlayers()
        mediaPlayer.stop()
        mediaPlayer.reset()
    }
}
