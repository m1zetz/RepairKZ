package com.example.repairkz.common.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.repairkz.R
import com.example.repairkz.common.models.User
import com.example.repairkz.ui.features.UserInfo.UserIntent

@Composable
fun ProfileString(
    user: User,
    intent: () -> Unit = {},
    descriptionPrefix: String = "",
) {

    val displayDescription = if(descriptionPrefix.isNotEmpty()){
        "${descriptionPrefix} ${stringResource(user.status.resID)}"
    }else{
        stringResource(user.displayDescriptionRes)
    }

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
            AsyncImage(
                model = user.userPhotoUrl ?: R.drawable.ic_launcher_background,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically)

            )

            Spacer(modifier = Modifier.size(12.dp))
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .align(Alignment.Top),
            ) {
                Text(
                    "${user.firstName} ${user.lastName}",
                    fontSize = 18.sp
                )
                Text(
                    displayDescription,
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
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = ImeAction.Done
                ),
                value = value,
                onValueChange = { newValue ->
                    changeValue(newValue)
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
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

@Composable
fun ProfileMainActions(
    @StringRes titleResId: Int,
    icon: ImageVector,
    action: UserIntent,
    onAction: (UserIntent) -> Unit,
    modifier: Modifier,
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
            .padding(4.dp)
            .height(110.dp)
            .shadow(6.dp, RoundedCornerShape(24.dp)),
        onClick = {
            onAction(action)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(text = stringResource(titleResId))
        }
    }
}


@Composable
fun StandartString(
    textR: Int,
    intent: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onSurface,
    icon: ImageVector? = null,
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

            icon?.let { icon ->
                Icon(icon, null)
                Spacer(modifier = Modifier.size(8.dp))
            }
            Text(
                text = stringResource(textR),
                color = color
            )
        }
    }
}
