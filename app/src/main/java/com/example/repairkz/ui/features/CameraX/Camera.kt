package com.example.repairkz.ui.features.CameraX


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.CrueltyFree
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import java.io.ByteArrayOutputStream
import androidx.core.net.toUri
import com.example.repairkz.ui.features.UserInfo.UserInfoViewModel
import com.example.repairkz.ui.features.UserInfo.UserIntent
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Camera(context: Context, takeNewPhoto:(Uri?)-> Unit) {
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }
    val scaffoldState = rememberBottomSheetScaffoldState()

    var bitmap = remember { mutableStateOf<Bitmap?>(null) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {

        }
    ) { paddingValues ->
        if (bitmap.value == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                CameraPreview(
                    controller = controller,
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(
                        onClick = {
                            takePhoto(controller, onPhotoTaken = { newBitMap ->
                                bitmap.value = newBitMap
                            }, context)
                        }
                    ) {
                        Icon(Icons.Default.Camera, null)
                    }

                    IconButton(
                        onClick = {
                            controller.cameraSelector =
                                if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                    CameraSelector.DEFAULT_FRONT_CAMERA
                                } else {
                                    CameraSelector.DEFAULT_BACK_CAMERA
                                }
                        }
                    ) {
                        Icon(Icons.Default.SwapHoriz, null)
                    }
                }
            }


        } else{
            PhotoPreview(context,bitmap.value!!, save = {uri ->
                takeNewPhoto(uri)
            })
        }
    }
}


private fun takePhoto(
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

@SuppressLint("UseKtx")
fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
    val cacheFile = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
    FileOutputStream(cacheFile).use { fos ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
    }
    return Uri.fromFile(cacheFile)
}

fun getImageBitmapFromUri(context: Context, uri: Uri?): Bitmap {
    return MediaStore.Images.Media.getBitmap(context.contentResolver,uri)
}
@Composable
fun PhotoPreview(context: Context, bitmap: Bitmap, save: (Uri?) -> Unit) {
    var uri by remember { mutableStateOf<Uri?>(null) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val cropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                uri = result.uriContent
            }
        }
        AsyncImage(
            model = uri ?: bitmap,
            contentDescription = null,
            modifier = Modifier.align(
                Alignment.Center
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(
                onClick = {
                    cropLauncher.launch(
                        CropImageContractOptions(
                            cropImageOptions = CropImageOptions(
                                guidelines = CropImageView.Guidelines.ON,

                                ),
                            uri = getImageUriFromBitmap(context, bitmap)
                        )
                    )
                }
            ) {
                Icon(Icons.Default.Crop, null)
            }
            IconButton(
                onClick = {
                    save(uri?: getImageUriFromBitmap(context,bitmap))
                }
            ) {
                Icon(Icons.Default.CrueltyFree, null)
            }
        }
    }
}


@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier,
) {
    
    val lifeCycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifeCycleOwner)
            }
        },
        modifier = modifier
    )
}