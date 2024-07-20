package com.example.kotlintest

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

class MediaPlayerManager (context: Context){
    private val player = MediaPlayer()

    init {
        player.setDataSource(context, Uri.parse("android.resource://" + context.packageName + "/" + R.raw.blink))
        player.prepare()
    }

    fun play(onComplete: () -> Unit){
        player.start()

        player.setOnCompletionListener {
            onComplete()
        }
    }
}