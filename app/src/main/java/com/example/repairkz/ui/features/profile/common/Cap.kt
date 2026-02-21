package com.example.repairkz.ui.features.profile.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.repairkz.R
import com.example.repairkz.ui.features.UserInfo.CommonInfo
import com.example.repairkz.ui.features.UserInfo.UserIntent

@Composable
fun Cap(
    commonInfo: CommonInfo,
    changeAvatarIntent: (UserIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp, start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.size(140.dp),
                    contentAlignment = Alignment.BottomEnd
                ){
                    AsyncImage(
                        model = commonInfo.photoUrl.ifEmpty { R.drawable.ic_launcher_background },
                        contentDescription = "UserPhoto",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                    if(commonInfo.isMe){
                        IconButton(
                            onClick = {changeAvatarIntent(UserIntent.OpenSheet)}
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(commonInfo.firstName, fontSize = 28.sp)
                Spacer(modifier = Modifier.size(4.dp))
                Text(commonInfo.lastName, fontSize = 28.sp)
            }
        }


    }

}
