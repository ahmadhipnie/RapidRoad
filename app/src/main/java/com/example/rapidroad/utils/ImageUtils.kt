package com.example.rapidroad.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.rapidroad.BuildConfig
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
private const val DATE_FORMAT = "yyyyMMdd_HHmmss"
private val currentTime: String = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(Date())

fun getImageUri(context: Context): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$currentTime.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCameraApp/")
        }
        context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        ) ?: fallbackUri(context)
    } else {
        fallbackUri(context)
    }
}

private fun fallbackUri(context: Context): Uri {
    val pictureDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val photoFile = File(pictureDir, "/RapidRoad/$currentTime.jpg")
    if (!photoFile.parentFile?.exists()!!) photoFile.parentFile?.mkdirs()
    return FileProvider.getUriForFile(
        context,
        "${BuildConfig.APPLICATION_ID}.provider",
        photoFile
    )
}