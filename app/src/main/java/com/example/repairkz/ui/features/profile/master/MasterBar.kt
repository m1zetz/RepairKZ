package com.example.repairkz.ui.features.profile.master

import com.example.repairkz.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.repairkz.common.ui.ProfileMainActions
import com.example.repairkz.ui.features.UserInfo.UserIntent

@Composable
fun MasterBar(onIntent: (UserIntent) -> Unit, masterId: Int){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        ProfileMainActions(
            R.string.do_order,
            Icons.Default.Build,
            UserIntent.MasterProfileIntent.DoOrder(masterId),
            onAction = onIntent,
            modifier = Modifier.weight(1f)
        )
        ProfileMainActions(
            R.string.to_favorites,
            Icons.Default.FavoriteBorder,
            UserIntent.MasterProfileIntent.AddToFavorites(masterId),
            onAction = onIntent,
            modifier = Modifier.weight(1f)
        )
        ProfileMainActions(
            R.string.report,
            Icons.Default.Report,
            UserIntent.MasterProfileIntent.Report(masterId),
            onAction = onIntent,
            modifier = Modifier.weight(1f)
        )

    }
}