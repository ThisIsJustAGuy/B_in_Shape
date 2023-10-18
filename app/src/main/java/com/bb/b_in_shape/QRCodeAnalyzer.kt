package com.bb.b_in_shape

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer

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
