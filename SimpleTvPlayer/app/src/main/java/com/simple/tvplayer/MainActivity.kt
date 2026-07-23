package com.simple.tvplayer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.leanback.widget.VerticalGridView
import com.simple.tvplayer.model.Channel
import com.simple.tvplayer.util.ConfigLoader
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var gridView: VerticalGridView
    private lateinit var tvLoading: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridView = findViewById(R.id.gridView)
        tvLoading = findViewById(R.id.tvLoading)

        // 每行显示4个频道卡片
        gridView.numberOfColumns = 4

        loadChannels()
    }

    private fun loadChannels() {
        thread {
            try {
                val channels = ConfigLoader.loadChannels()
                runOnUiThread {
                    showChannels(channels)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    tvLoading.text = getString(R.string.load_error)
                }
            }
        }
    }

    private fun showChannels(channels: List<Channel>) {
        tvLoading.visibility = View.GONE
        gridView.visibility = View.VISIBLE

        val adapter = ChannelAdapter(channels) { channel ->
            // 点击频道，进入播放页
            val intent = Intent(this, PlayerActivity::class.java).apply {
                putExtra(PlayerActivity.EXTRA_CHANNEL_NAME, channel.name)
                putExtra(PlayerActivity.EXTRA_CHANNEL_URL, channel.url)
            }
            startActivity(intent)
        }
        gridView.adapter = adapter
    }
}
