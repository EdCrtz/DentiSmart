package com.dreamteam.dentismart.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import java.io.ByteArrayOutputStream


object Rutinas {
    private val DEFAULT_BITMAP = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    fun convertBase64ToBitMap(base64img: String?): Bitmap? {
        val decodedString: ByteArray = Base64.decode(base64img ?: "", Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        val bytes = ByteArrayOutputStream()
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes)
            val byteArray: ByteArray = bytes.toByteArray()
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
        return DEFAULT_BITMAP
    }

    fun generateDominantColor(bitmap: Bitmap?): Int {
        if (null == bitmap) return Color.TRANSPARENT
        var redBucket = 0
        var greenBucket = 0
        var blueBucket = 0
        var alphaBucket = 0
        val hasAlpha = bitmap.hasAlpha()
        val pixelCount = bitmap.width * bitmap.height
        val pixels = IntArray(pixelCount)
        bitmap.getPixels(
            pixels,
            0,
            bitmap.width,
            0,
            0,
            bitmap.width,
            bitmap.height
        )
        var y = 0
        val h = bitmap.height
        while (y < h) {
            var x = 0
            val w = bitmap.width
            while (x < w) {
                val color = pixels[x + y * w] // x + y * width
                redBucket += color shr 16 and 0xFF // Color.red
                greenBucket += color shr 8 and 0xFF // Color.greed
                blueBucket += color and 0xFF // Color.blue
                if (hasAlpha) alphaBucket += color ushr 24 // Color.alpha
                x++
            }
            y++
        }
        return Color.argb(
            if (hasAlpha) alphaBucket / pixelCount else 255,
            redBucket / pixelCount,
            greenBucket / pixelCount,
            blueBucket / pixelCount
        )
    }
}