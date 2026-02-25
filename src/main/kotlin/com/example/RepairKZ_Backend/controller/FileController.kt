package com.example.RepairKZ_Backend.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream

@RestController
@RequestMapping("/files")
class FileController {
    @PostMapping
    fun savePhoto(@RequestPart file: MultipartFile){

    }
}