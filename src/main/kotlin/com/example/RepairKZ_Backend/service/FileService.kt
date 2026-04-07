package com.example.RepairKZ_Backend.service

import com.cloudinary.Cloudinary

import com.cloudinary.utils.ObjectUtils
import com.example.RepairKZ_Backend.common.PHOTO_DIRECTORY
import com.example.RepairKZ_Backend.common.PHOTO_URL_PATH

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.UUID

@Service
class FileService(
    private val cloudinary: Cloudinary
) {

    fun savePhotoAndGetUrlCloudinary(rawFile : MultipartFile?) : String?{
        return try {
            rawFile?.let{
                val fileName = UUID.randomUUID().toString()
                val response = cloudinary.uploader().upload(rawFile.bytes, ObjectUtils.asMap("public_id", fileName))
                val url = response["secure_url"] as? String
                url
            }
        } catch (e: Exception){
            null
        }
    }

    fun savePhotoAndGetUrl(rawFile: MultipartFile?): String? {
        if (rawFile == null) {
            return null
        } else {
            val randomUUID = UUID.randomUUID()
            val content = rawFile.bytes
            val imageDirectory = File("$PHOTO_DIRECTORY$randomUUID.jpg")
            imageDirectory.writeBytes(content)
            val ip = "172.20.10.5   "
            return "http://$ip:8080/$PHOTO_URL_PATH/$randomUUID.jpg"
        }
    }

}