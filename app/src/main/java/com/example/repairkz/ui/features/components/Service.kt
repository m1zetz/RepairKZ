package com.example.repairkz.ui.features.components

import com.example.repairkz.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.repairkz.data.remote.dto.MasterServiceDTO

@Composable
fun Service(
    item: MasterServiceDTO,
    onDelete: (() -> Unit)? = null,
    onEdit: (() -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(10.dp),

            ) {
            Text(
                item.service, modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .align(Alignment.Top),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "${item.price} ₸", modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.Top)
                        .weight(2f),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.Top),
                    contentAlignment = Alignment.TopEnd
                ) {
                    onEdit?.let {
                        IconButton(
                            modifier = Modifier
                                .size(20.dp),
                            onClick = {
                                expanded = true
                            }
                        ) {
                            Icon(Icons.Default.MoreVert, null, modifier = Modifier.size(20.dp))
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                onClick = { onEdit?.invoke()},
                                text = { Text(stringResource(R.string.edit)) },
                                trailingIcon = {
                                    Icon(Icons.Default.Edit,null)
                                }
                            )
                            DropdownMenuItem(
                                onClick = { onDelete?.invoke()},
                                text = { Text(stringResource(R.string.delete)) },
                                trailingIcon = {
                                    Icon(Icons.Default.Delete,null)
                                }
                            )
                        }
                    }

                }

            }


        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
    }

}