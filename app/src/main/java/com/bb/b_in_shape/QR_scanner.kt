package com.bb.b_in_shape

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
@SuppressLint("RestrictedApi")
class QR_scanner : ComponentActivity(){
    private lateinit var cameraExecutor: ExecutorService

    companion object {
        private const val MY_PERMISSIONS_REQUEST_CAMERA = 1
        private const val TAG = "MainActivity"
    }

    @SuppressLint("MissingInflatedId", "RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)

        cameraExecutor = Executors.newSingleThreadExecutor()

        val webView: WebView = findViewById(R.id.web_view)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        val previewView: PreviewView = findViewById(R.id.preview_view)
        startCamera(previewView)

    }

    private fun startCamera(previewView: PreviewView) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "We need camera access to scan QR codes", Toast.LENGTH_LONG).show()
            }
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                MY_PERMISSIONS_REQUEST_CAMERA)
            return
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setImageQueueDepth(3) // Number of frames of images in the queue
            .build()
            .also {
                it.setAnalyzer(ContextCompat.getMainExecutor(this), QRCodeAnalyzer { url ->
                    val webView: WebView = findViewById(R.id.web_view)
                    webView.loadData(stringMaker(url), "text/html", "utf-8")
                })
            }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            cameraProvider.unbindAll()

            try {
                cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageAnalysis)
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
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

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val previewView: PreviewView = findViewById(R.id.preview_view)
                    startCamera(previewView)  // <-- Here you call startCamera without the previewView argument
                } else {
                    // Permission was denied. Disable the functionality that depends on this permission.
                    Toast.makeText(this, "Camera permission is required to use the scanner", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                // Handle other permissions if any
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
class QRCodeAnalyzer(private val onQRCodeDetected: (String) -> Unit) : ImageAnalysis.Analyzer {
    private val reader = MultiFormatReader()
    companion object {
        private const val TAG = "QRCodeAnalyzer"
    }
    override fun analyze(image: ImageProxy) {
        Log.d(TAG, "Analyzing frame...")

        val yBuffer = image.planes[0].buffer
        val ySize = yBuffer.remaining()
        val yArray = ByteArray(ySize)
        yBuffer[yArray]

        val source = PlanarYUVLuminanceSource(
            yArray,
            image.width,
            image.height,
            0,
            0,
            image.width,
            image.height,
            false
        )
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

        try {
            val result = reader.decode(binaryBitmap)
            onQRCodeDetected(result.text)
            Log.d(TAG, "QR code detected: ${result.text}")
        } catch (e: NotFoundException) {
            Log.e(TAG, "QR code not found", e)
            // No QR code found
        } finally {
            image.close()
        }
    }
}