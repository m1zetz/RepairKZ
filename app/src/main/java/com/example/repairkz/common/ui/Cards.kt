package com.example.repairkz.common.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.repairkz.R

@Composable
fun ProfileString(imageUrl: String?, name: String, description: String, intent: () -> Unit = {}) {
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
            imageUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = "UserPhoto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically)

                )
            } ?: run {
                AsyncImage(
                    model = R.drawable.ic_launcher_background,
                    contentDescription = "UserPhoto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterVertically)

                )
            }

            Spacer(modifier = Modifier.size(12.dp))
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .align(Alignment.Top),
            ) {
                Text(
                    name,
                    fontSize = 18.sp
                )
                Text(
                    description,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun ShortInfoCard(titleResID: Int, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)

    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = stringResource(titleResID),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun ShortInputCard(
    @StringRes titleResID: Int,
    @StringRes placeholderResId: Int,
    value: String,
    changeValue: (newValue: String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),

        ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = stringResource(titleResID),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.size(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                value = value,
                onValueChange = { newValue ->
                    changeValue(newValue)
                },
                shape = MaterialTheme.shapes.medium,
                placeholder = { Text(stringResource(placeholderResId)) }
            )

        }
    }
}

@Composable
fun ShortWithComposableCard(titleResID: Int, Сomposable: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)

    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = stringResource(titleResID),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.size(8.dp))
            Сomposable()
        }
    }
}