# SimpleTvPlayer 简易电视播放器

一款专为老人设计的极简安卓 TV 直播播放器，打开即用，零设置。

## ✨ 功能特点

- **极简界面**：打开只有频道卡片，没有任何设置菜单
- **老人友好**：大字体、大按钮、高对比度，遥控器操作简单
- **远程配置**：频道数量、名称、播放地址全部由远程 JSON 文件控制
- **即点即播**：选中频道按确认键直接播放，无需多余操作
- **自动全屏**：播放自动全屏，顶部频道名 3 秒后自动隐藏
- **Android TV 原生支持**：适配电视桌面 Leanback 启动器

## 📁 项目结构

```
SimpleTvPlayer/
├── app/
│   └── src/main/
│       ├── java/com/simple/tvplayer/
│       │   ├── MainActivity.kt        # 主界面 - 频道列表
│       │   ├── PlayerActivity.kt      # 播放页
│       │   ├── ChannelAdapter.kt      # 频道卡片适配器
│       │   ├── model/Channel.kt       # 频道数据类
│       │   └── util/ConfigLoader.kt   # 远程配置加载器
│       ├── res/                       # 资源文件
│       └── AndroidManifest.xml
├── build.gradle.kts
├── settings.gradle.kts
└── .github/workflows/build.yml        # GitHub 自动编译
```

## 🚀 快速开始

### 第一步：准备远程配置文件

在你自己的域名下创建一个 JSON 文件（例如 `https://your-domain.com/channels.json`），内容格式如下：

```json
{
  "channels": [
    { "name": "CCTV-1 综合", "url": "https://example.com/cctv1.m3u8" },
    { "name": "CCTV-2 财经", "url": "https://example.com/cctv2.m3u8" },
    { "name": "CCTV-5 体育", "url": "https://example.com/cctv5.m3u8" },
    { "name": "湖南卫视",   "url": "https://example.com/hunan.m3u8" },
    { "name": "广东卫视",   "url": "https://example.com/guangdong.m3u8" }
  ]
}
```

- `name`：频道显示名称（支持中文）
- `url`：视频流地址（支持 m3u8、mp4、flv 等 ExoPlayer 支持的格式）
- 频道数量不限，想加多少加多少

### 第二步：修改配置地址

打开文件 `app/src/main/java/com/simple/tvplayer/util/ConfigLoader.kt`，修改第一行常量：

```kotlin
// 改为你自己的配置文件地址
const val CONFIG_URL = "https://your-domain.com/channels.json"
```

### 第三步：编译安装

#### 方式一：GitHub Actions 自动编译（推荐）

1. 将本项目上传到你的 GitHub 仓库
2. 进入仓库的 **Actions** 页面，找到 **Build APK** 工作流
3. 点击 **Run workflow** 开始编译
4. 编译完成后，在工作流详情页的 **Artifacts** 中下载 `app-debug.apk`
5. 安装到电视盒子即可

#### 方式二：本地编译

需要 Android Studio 或 Android SDK：

```bash
# Linux / macOS
./gradlew assembleDebug

# Windows
gradlew.bat assembleDebug
```

编译产物：`app/build/outputs/apk/debug/app-debug.apk`

## 🎮 使用说明

### 遥控器操作

| 按键 | 功能 |
|------|------|
| 方向键 | 移动选择频道 |
| 确认/OK | 播放选中的频道 |
| 返回 | 退出播放 / 返回频道列表 |

### 老人使用流程

1. 打开 APP → 看到频道列表
2. 按上下左右选频道 → 蓝色高亮就是选中的
3. 按确认键 → 开始播放
4. 按返回键 → 回到频道列表

就这么简单，没有其他任何操作。

## 🔧 远程更新频道

**不需要重新编译安装 APP！**

只需要修改你域名下的 `channels.json` 文件内容，下次打开 APP 时自动加载最新的频道列表。

- 增加频道：在 `channels` 数组里加一项
- 删除频道：从数组里删掉
- 修改名称/地址：直接改对应的字段

## 📺 支持的视频格式

基于 ExoPlayer，支持：
- HLS (m3u8) 直播流
- DASH (mpd)
- MP4、MKV、AVI 等常见格式
- RTMP（需额外配置）

## ⚙️ 自定义调整

### 修改每行显示的频道数

打开 `MainActivity.kt`，修改：

```kotlin
gridView.numberOfColumns = 4  // 改为你想要的数量，如 3、5
```

### 修改卡片大小

打开 `res/layout/item_channel.xml`，修改根布局的 `layout_width` 和 `layout_height`。

### 修改主题颜色

打开 `res/drawable/channel_bg_selector.xml`，修改 `solid` 的 `color` 值。

## 📋 系统要求

- 最低 Android 版本：5.0 (API 21)
- 目标 Android 版本：14 (API 34)
- 支持设备：Android TV 电视、电视盒子、投影仪

## 📄 开源协议

MIT License
