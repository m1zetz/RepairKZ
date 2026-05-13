package com.example.repairkz.ui.features.profile.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.repairkz.common.ui.UserPhoto
import com.example.repairkz.ui.features.UserInfo.BusinessCardData

@Composable
fun Cap(
    businessCardData: BusinessCardData,
    changeAvatarIntent: (() -> Unit)? = null,
    isLoading: Boolean? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ,
            shape = RoundedCornerShape(12.dp)
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
                    UserPhoto(
                        businessCardData.photoUrl,
                        businessCardData.isMe,
                        {changeAvatarIntent?.invoke()},
                        isLoading = isLoading?:false
                    )

                }



            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(businessCardData.firstName, fontSize = 28.sp)
                Spacer(modifier = Modifier.size(4.dp))
                Text(businessCardData.lastName, fontSize = 28.sp)
            }
        }


    }

}
