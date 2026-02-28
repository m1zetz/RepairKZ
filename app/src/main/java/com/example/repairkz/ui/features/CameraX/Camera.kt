package com.example.repairkz.ui.features.CameraX

import android.app.Activity
import com.example.repairkz.R
import com.example.repairkz.common.utils.takePhoto
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.CrueltyFree
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.example.repairkz.common.extensions.getActivityOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Camera(context: Context, takeNewPhoto: (Uri?) -> Unit) {

    DisposableEffect(Unit) {

        val activity = context.getActivityOrNull()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }
    val scaffoldState = rememberBottomSheetScaffoldState()

    val uri = rememberSaveable { mutableStateOf<Uri?>(null) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {

        }
    ) { paddingValues ->
        if (uri.value == null) {
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
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.weight(1f))
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        IconButton(
                            onClick = {
                                takePhoto(controller, onPhotoTaken = { newUri ->
                                    uri.value = newUri
                                }, context)
                            },
                            Modifier
                                .size(80.dp)

                        ) {
                            Icon(
                                Icons.Default.Circle, null, Modifier
                                    .size(80.dp),
                                tint = Color.White
                            )
                        }
                    }
                    Box(Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                        IconButton(
                            onClick = {
                                controller.cameraSelector =
                                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                        CameraSelector.DEFAULT_FRONT_CAMERA
                                    } else {
                                        CameraSelector.DEFAULT_BACK_CAMERA
                                    }
                            },
                            Modifier
                                .padding(end = 32.dp)
                                .size(56.dp)

                        ) {
                            Icon(
                                Icons.Default.SwapHoriz, null,
                                Modifier.size(30.dp),
                                tint = Color.White
                            )
                        }
                    }


                }
            }


        } else {
            BackHandler {
                uri.value = null
            }
            PhotoPreview(context, uri.value!!, save = { uri ->
                takeNewPhoto(uri)
            },
                onDismissRequest = {
                    uri.value = null
                })
        }
    }
}


@Composable
fun PhotoPreview(
    context: Context,
    uri: Uri,
    onDismissRequest: () -> Unit,
    save: (Uri?) -> Unit,
) {
    var newUri by remember { mutableStateOf<Uri?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val cropLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
                if (result.isSuccessful) {
                    val croppedUri = result.uriContent
                    newUri = croppedUri
                }
            }
            AsyncImage(
                model = newUri ?: uri,
                contentDescription = null,
                modifier = Modifier
                    .align(
                        Alignment.Center
                    )
                    .fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    onClick = {
                        cropLauncher.launch(
                            CropImageContractOptions(
                                cropImageOptions = CropImageOptions(
                                    guidelines = CropImageView.Guidelines.ON,
                                    activityBackgroundColor = ContextCompat.getColor(
                                        context,
                                        R.color.crop_background
                                    )
                                ),
                                uri = uri

                            )
                        )

                    },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        Icons.Default.Crop, null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                IconButton(
                    onClick = {
                        save(newUri ?: uri)
                    },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        Icons.Default.Check, null, tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                IconButton(
                    onClick = {
                        onDismissRequest()
                    },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
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
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        AndroidView(
            factory = {
                PreviewView(it).apply {
                    this.controller = controller
                    controller.bindToLifecycle(lifeCycleOwner)
                }
            },
            modifier = modifier.aspectRatio(3f / 4f)

        )
    }

}