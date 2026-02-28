package com.example.repairkz.domain.useCases.files

import android.net.Uri
import com.example.repairkz.data.fileData.FileRepository
import jakarta.inject.Inject

class SaveToInternalUseCase @Inject constructor(
    private val fileRepository: FileRepository
){
    suspend operator fun invoke(uri: Uri?) : Uri?{
        return fileRepository.saveToInternalStorage(uri)
    }
}