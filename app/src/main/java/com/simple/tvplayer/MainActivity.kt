package com.simple.tvplayer

import android.content.Context
import android.os.Bundle
import android.widget.GridLayoutManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.simple.tvplayer.model.Channel
import com.simple.tvplayer.util.ConfigLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChannelAdapter
    private val channelList = mutableListOf<Channel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rv_channel)
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        adapter = ChannelAdapter(channelList) { channel ->
            PlayerActivity.jump(this, channel.url)
        }
        recyclerView.adapter = adapter
        loadRemoteChannels()
    }

    private fun loadRemoteChannels() {
        GlobalScope.launch(Dispatchers.IO) {
            val result = ConfigLoader.getChannelList()
            withContext(Dispatchers.Main) {
                if (result.isNotEmpty()) {
                    channelList.clear()
                    channelList.addAll(result)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@MainActivity, "网络异常，频道加载失败", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
