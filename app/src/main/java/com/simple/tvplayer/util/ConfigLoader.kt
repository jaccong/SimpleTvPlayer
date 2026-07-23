package com.simple.tvplayer.util

import com.simple.tvplayer.model.Channel
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 远程配置加载器
 * 从指定URL加载JSON格式的频道列表
 *
 * JSON格式示例：
 * {
 *   "channels": [
 *     { "name": "CCTV-1 综合", "url": "https://example.com/stream1.m3u8" },
 *     { "name": "CCTV-2 财经", "url": "https://example.com/stream2.m3u8" }
 *   ]
 * }
 */
object ConfigLoader {

    // ====== 在此处修改为你的配置文件地址 ======
    const val CONFIG_URL = "https://j.877622.xyz/api/get-json"
    // =========================================

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    /**
     * 同步加载频道列表（需在子线程调用）
     */
    fun loadChannels(): List<Channel> {
        val request = Request.Builder()
            .url(CONFIG_URL)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("加载配置失败: ${response.code}")
            val body = response.body?.string() ?: throw IOException("配置内容为空")
            return parseJson(body)
        }
    }

    private fun parseJson(json: String): List<Channel> {
        val obj = JSONObject(json)
        val arr = obj.getJSONArray("channels")
        val list = mutableListOf<Channel>()
        for (i in 0 until arr.length()) {
            val item = arr.getJSONObject(i)
            list.add(
                Channel(
                    name = item.getString("name"),
                    url = item.getString("url")
                )
            )
        }
        return list
    }
}
