package com.example.repairkz.data.fileData

import android.net.Uri

interface FileRepository {
    suspend fun saveToInternalStorage(uri: Uri?): Uri?
}