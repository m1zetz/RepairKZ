package com.example.RepairKZ_Backend.service

import com.example.RepairKZ_Backend.common.PHOTO_DIRECTORY
import com.example.RepairKZ_Backend.repository.FileRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.UUID

@Service
class FileService() {

    fun updateUserPhoto(file : MultipartFile?) : String?{
        if(file == null){
            return null
        } else{
            val randomUUID = UUID.randomUUID()
            val content = file.bytes
            val imageDirectory = File("$PHOTO_DIRECTORY$randomUUID.jpg")
            imageDirectory.writeBytes(content)
            return imageDirectory.absolutePath
        }
    }

}