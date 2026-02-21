package com.example.repairkz.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream


fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
    val cacheFile = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
    FileOutputStream(cacheFile).use { fos ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
    }
    return Uri.fromFile(cacheFile)
}

fun takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    context: Context,
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val bitmap = image.toBitmap()
                val angle = image.imageInfo.rotationDegrees
                val matrix = Matrix()
                if(controller.cameraSelector ==  CameraSelector.DEFAULT_FRONT_CAMERA){
                    matrix.postScale(1.0f, -1.0f)
                }
                matrix.postRotate(angle.toFloat())
                val rotatedBitmap = Bitmap.createBitmap(
                    bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
                )

                onPhotoTaken(rotatedBitmap)
                image.close()
            }
            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
            }
        }
    )
}