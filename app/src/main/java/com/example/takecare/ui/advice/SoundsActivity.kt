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
                (it as ImageButton).background = getDrawable(R.drawable.rounded_rectangle_green)
                (it as ImageButton).setImageResource(R.drawable.ic_pause_black_24dp)
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
                (it as ImageButton).background = getDrawable(R.drawable.rounded_rectangle_green)
                (it as ImageButton).setImageResource(R.drawable.ic_pause_black_24dp)
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
                (it as ImageButton).background = getDrawable(R.drawable.rounded_rectangle_green)
                (it as ImageButton).setImageResource(R.drawable.ic_pause_black_24dp)
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
                (it as ImageButton).background = getDrawable(R.drawable.rounded_rectangle_green)
                (it as ImageButton).setImageResource(R.drawable.ic_pause_black_24dp)
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
                (it as ImageButton).background = getDrawable(R.drawable.rounded_rectangle_green)
                (it as ImageButton).setImageResource(R.drawable.ic_pause_black_24dp)
                mediaPlayer.setDataSource(getString(R.string.sounds_5_url))
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        }
    }

    private fun resetPlayers(){
        (sound_1 as ImageButton).background = getDrawable(R.drawable.rounded_rectangle)
        (sound_1 as ImageButton).setImageResource(R.drawable.ic_play_arrow_black_24dp)

        (sound_2 as ImageButton).background = getDrawable(R.drawable.rounded_rectangle)
        (sound_2 as ImageButton).setImageResource(R.drawable.ic_play_arrow_black_24dp)

        (sound_3 as ImageButton).background = getDrawable(R.drawable.rounded_rectangle)
        (sound_3 as ImageButton).setImageResource(R.drawable.ic_play_arrow_black_24dp)

        (sound_4 as ImageButton).background = getDrawable(R.drawable.rounded_rectangle)
        (sound_4 as ImageButton).setImageResource(R.drawable.ic_play_arrow_black_24dp)

        (sound_5 as ImageButton).background = getDrawable(R.drawable.rounded_rectangle)
        (sound_5 as ImageButton).setImageResource(R.drawable.ic_play_arrow_black_24dp)

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
