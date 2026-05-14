package com.example.repairkz.ui.features.components

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.repairkz.R
import com.example.repairkz.common.models.MasterService
import com.example.repairkz.data.remote.dto.MasterServiceDTO
import com.example.repairkz.ui.features.UserInfo.UserIntent

@Composable
fun ServicesTable(
    services: List<MasterService>,
    onCreate: (() -> Unit)? = null,
    onDelete: ((Long) -> Unit)? = null,
    onEdit: (() -> Unit)? = null,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
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
                    stringResource(R.string.service_name),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .align(Alignment.Top),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
                VerticalDivider(
                    Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.Top)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        stringResource(R.string.price_in_tenge),
                        modifier = Modifier
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
                                    onCreate!!.invoke()
                                }
                            ) {
                                Icon(Icons.Default.Add, null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
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
            services.sortedBy {
                it.position
            }.forEach { item ->
                Service(
                    item,
                    onDelete = if(onDelete != null) {{onDelete.invoke(item.id)}} else null,
                    onEdit = if(onEdit != null) {{onEdit.invoke()}} else null
                )
            }
            Spacer(Modifier.size(8.dp))
        }

    }
}