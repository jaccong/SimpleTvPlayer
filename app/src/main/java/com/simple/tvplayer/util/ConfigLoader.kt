package com.simple.tvplayer.util

import com.simple.tvplayer.model.Channel
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

// 这里改成你自己域名存放channels.json的地址
const val CONFIG_URL = "https://j.877622.xyz/api/get-json"

object ConfigLoader {
    private val client = OkHttpClient()

    suspend fun getChannelList(): List<Channel> {
        val channelList = mutableListOf<Channel>()
        val request = Request.Builder()
            .url(CONFIG_URL)
            .build()

        return try {
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: return emptyList()
            val jsonObj = JSONObject(body)
            val jsonArr: JSONArray = jsonObj.getJSONArray("channels")

            for (i in 0 until jsonArr.length()) {
                val item = jsonArr.getJSONObject(i)
                val name = item.getString("name")
                val url = item.getString("url")
                channelList.add(Channel(name, url))
            }
            channelList
        } catch (e: IOException) {
            emptyList()
        }
    }
}
