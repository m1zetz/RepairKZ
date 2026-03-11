package com.example.repairkz.common.ui

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.repairkz.R
import kotlin.text.ifEmpty

@Composable
fun UserPhoto(
    photoUri: String?,
    isMe: Boolean = true,
    changeAvatarIntent: () -> Unit,
) {
    Box(
        modifier = Modifier.size(140.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        AsyncImage(
            model = if (photoUri.isNullOrEmpty()) { R.drawable.ic_launcher_background } else photoUri,
            contentDescription = "UserPhoto",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        )
        if (isMe) {
            IconButton(
                onClick = {
                    changeAvatarIntent()
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