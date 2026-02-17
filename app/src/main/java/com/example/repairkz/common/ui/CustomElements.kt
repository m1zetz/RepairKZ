package com.example.repairkz.common.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource

interface DisplayableEnum {
    @get:StringRes
    val resID: Int
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> EnumDropDown(
    @StringRes patternResId: Int,
    selectedItem: T?,
    elements: List<T>,
    onSelect: (T) -> Unit
) where T : Enum<T>, T : DisplayableEnum {

    var menuIsOpen by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    ExposedDropdownMenuBox(
        expanded = menuIsOpen,
        onExpandedChange = {
            menuIsOpen = !menuIsOpen
        }
    ) {
        OutlinedTextField(
            value = stringResource(selectedItem?.resID ?: patternResId),
            readOnly = true,
            onValueChange = {},
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,

        )
        ExposedDropdownMenu(
            expanded = menuIsOpen,
            onDismissRequest = {
                menuIsOpen = false
            },

        ) {
            elements.forEach { item ->
                DropdownMenuItem(
                    text = { Text(stringResource(item.resID)) },
                    onClick = {
                        onSelect(item)
                        menuIsOpen = false
                        focusManager.clearFocus()
                    }
                )
            }
        }
    }
}
