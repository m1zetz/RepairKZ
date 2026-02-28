package com.example.repairkz.data.fileData

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class FileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FileRepository {
    override suspend fun saveToInternalStorage(uri: Uri?) : Uri?{
        return withContext(Dispatchers.IO){
            try {
                uri?.let{
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val file = File(context.filesDir, "avatar_${System.currentTimeMillis()}.jpg")

                    inputStream?.use {input ->
                        FileOutputStream(file).use{ output ->
                            input.copyTo(output)
                        }
                        Uri.fromFile(file)
                    }
                }
            } catch (e: Exception){
                null
            }
        }

    }

}