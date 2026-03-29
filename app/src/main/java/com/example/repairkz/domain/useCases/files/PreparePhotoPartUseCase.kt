package com.example.repairkz.domain.useCases.files

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class PreparePhotoPartUseCase @Inject constructor() {
    operator fun invoke(context: Context, uri: Uri): MultipartBody.Part? {
        return try {
            val file = File(context.cacheDir, "temp_photo.jpg")
            context.contentResolver.openInputStream(uri).use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream!!.copyTo(outputStream)
                }
            }
            val requestBodyFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("file", file.name, requestBodyFile)
        } catch (e: Exception){
            null
        }

    }
}
