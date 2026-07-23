package com.simple.tvplayer

import android.os.Bundle
import android.view.View
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
        // 固定4列TV大卡片布局
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        adapter = ChannelAdapter(channelList) { channel ->
            // 点击跳播放页
            PlayerActivity.start(this, channel.url)
        }
        recyclerView.adapter = adapter
        loadRemoteChannels()
    }

    // 后台拉取远程JSON配置
    private fun loadRemoteChannels() {
        GlobalScope.launch(Dispatchers.IO) {
            val result = ConfigLoader.fetchChannels()
            withContext(Dispatchers.Main) {
                if (result.isNotEmpty()) {
                    channelList.clear()
                    channelList.addAll(result)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@MainActivity, "频道加载失败，请检查网络", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
