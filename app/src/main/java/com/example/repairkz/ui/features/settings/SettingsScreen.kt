package com.example.repairkz.ui.features.settings

import com.example.repairkz.R
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.repairkz.common.ui.ProfileString

@Composable
fun SettingsScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            ProfileString(
                "",
                intent = {},
                name = "Maxim Ius",
                description = ""
            )
            StandartString(
                R.string.payment_system,
                intent = {}
            )
            StandartString(
                R.string.themes,
                intent = {}
            )
            StandartString(
                R.string.exit,
                intent = {},
                color = MaterialTheme.colorScheme.error
            )
        }

    }
}

@Composable
fun StandartString(
    textR: Int,
    intent: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        onClick = {
            intent()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(textR),
                color = color
            )
        }
    }
}

