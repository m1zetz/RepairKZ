package com.example.repairkz.common.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.repairkz.R
import com.example.repairkz.common.constants.SERVER_IP
import kotlin.text.ifEmpty

@Composable
fun UserPhoto(
    photoUri: String?,
    isMe: Boolean = true,
    changeAvatarIntent: (() -> Unit)? = null,
    isLoading: Boolean = false,
    size: Dp? = null
) {
    Box(
        modifier = Modifier.size(size?:140.dp),
        contentAlignment = Alignment.Center
    ){
        if(isLoading)
            Box(modifier = Modifier.align(Alignment.TopEnd)){
               CircularProgressIndicator(modifier = Modifier.size(18.dp))
            }


        if (photoUri.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(0.6f),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            val modifier = if(size!= null) Modifier.size(size) else Modifier.fillMaxSize()
            AsyncImage(
                model = photoUri,
                contentDescription = "UserPhoto",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .clip(CircleShape)
            )
        }
        if (isMe) {
            Box(modifier = Modifier.align(Alignment.BottomEnd)){
                IconButton(
                    onClick = {
                        changeAvatarIntent?.invoke()
                    }
                ) {
                    Icon(
                        Icons.Outlined.CameraAlt,
                        null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

        }
    }

}