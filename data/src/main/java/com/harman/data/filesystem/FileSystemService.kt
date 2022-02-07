package com.harman.data.filesystem

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import java.io.IOException

class FileSystemService constructor(private val context: Context) {

    @Throws(IOException::class)
    fun <T> read(uri: Uri, classOfT: Class<T>): T {
        context.contentResolver.openInputStream(uri)?.let { inputStream ->
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer)
            return Gson().fromJson(json, classOfT)
        }
        throw IOException()
    }

    @Throws(IOException::class)
    fun <T> write(uri: Uri, obj: T) {
        val json = Gson().toJson(obj)
        context.contentResolver.openOutputStream(uri)?.let { outputStream ->
            outputStream.write(json.toByteArray())
            outputStream.close()
            return
        }
        throw IOException()
    }
}