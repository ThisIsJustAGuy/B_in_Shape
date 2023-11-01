package com.bb.b_in_shape

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent


class VideoActivity: ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContentView(R.layout.video_player)

            val intent= intent
            val url = intent.getStringExtra("url").toString()
            val webView: WebView = findViewById(R.id.web_view)
            webView.webViewClient = WebViewClient()
            webView.settings.javaScriptEnabled = true

            webView.loadData(stringMaker(url), "text/html", "utf-8")

            val bdp : String = intent.getStringExtra("bodypart").toString()
            val time : String = intent.getStringExtra("time").toString()
            var back_btn = findViewById<ImageButton>(R.id.Exercise_back)
            back_btn.setOnClickListener { _ -> navigateBack(bdp, time) }

            val stat = intent.getStringExtra("exercise").toString()
            val statusText = findViewById<TextView>(R.id.Status_tw)
            statusText.text = stat
        }
    }

    fun navigateBack(bdp: String, time: String) {
        val intent = Intent(this, ExerciseActivity::class.java)
        intent.putExtra("bodypart", bdp)
        intent.putExtra("time", time)

        startActivity(intent)
    }
    private fun stringMaker(url: String): String {
        return  "<html><body><!-- 1. The <iframe> (and video player) will replace this <div> tag. -->\n" +
                "    <div id=\"player\"></div>\n" +
                "\n" +
                "    <script>\n" +
                "      // 2. This code loads the IFrame Player API code asynchronously.\n" +
                "      var tag = document.createElement('script');\n" +
                "\n" +
                "      tag.src = \"https://www.youtube.com/iframe_api\";\n" +
                "      var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                "\n" +
                "      // 3. This function creates an <iframe> (and YouTube player)\n" +
                "      //    after the API code downloads.\n" +
                "      var player;\n" +
                "      function onYouTubeIframeAPIReady() {\n" +
                "        player = new YT.Player('player', {\n" +
                "          height: '100%',\n" +
                "          width: '100%',\n" +
                "          videoId: '$url',\n" +
                "          playerVars: {\n" +
                "            'playsinline': 1\n" +
                "          },\n" +
                "          events: {\n" +
                "            'onReady': onPlayerReady,\n" +
                "            'onStateChange': onPlayerStateChange\n" +
                "          }\n" +
                "        });\n" +
                "      }\n" +
                "\n" +
                "      // 4. The API will call this function when the video player is ready.\n" +
                "      function onPlayerReady(event) {\n" +
                "        event.target.playVideo();\n" +
                "      }\n" +
                "\n" +
                "      // 5. The API calls this function when the player's state changes.\n" +
                "      //    The function indicates that when playing a video (state=1),\n" +
                "      //    the player should play for six seconds and then stop.\n" +
                "      var done = false;\n" +
                "      function onPlayerStateChange(event) {\n" +
                "        if (event.data == YT.PlayerState.PLAYING && !done) {\n" +
                "          setTimeout(stopVideo, 6000);\n" +
                "          done = true;\n" +
                "        }\n" +
                "      }\n" +
                "      function stopVideo() {\n" +
                "        player.stopVideo();\n" +
                "      }\n" +
                "    </script></body></html>"
    }
}