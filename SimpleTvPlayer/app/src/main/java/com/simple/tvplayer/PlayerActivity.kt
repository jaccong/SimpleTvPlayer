package com.simple.tvplayer

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

class PlayerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CHANNEL_NAME = "channel_name"
        const val EXTRA_CHANNEL_URL = "channel_url"
    }

    private lateinit var playerView: PlayerView
    private lateinit var tvChannelName: TextView
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerView = findViewById(R.id.playerView)
        tvChannelName = findViewById(R.id.tvChannelName)

        val channelName = intent.getStringExtra(EXTRA_CHANNEL_NAME) ?: ""
        val channelUrl = intent.getStringExtra(EXTRA_CHANNEL_URL) ?: ""

        tvChannelName.text = channelName

        // 3秒后自动隐藏频道名称
        tvChannelName.postDelayed({
            tvChannelName.visibility = View.GONE
        }, 3000)

        initializePlayer(channelUrl)
    }

    private fun initializePlayer(url: String) {
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            playerView.player = exoPlayer
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    override fun onStart() {
        super.onStart()
        player?.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        player?.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}
