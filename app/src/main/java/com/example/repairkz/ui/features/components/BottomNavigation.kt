package com.example.repairkz.ui.features.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val unselected: ImageVector,
    val news: Boolean,
    val badgeCount: Int? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(selectedItemIndex: Int, changeScreen: (Int) -> Unit) {

    val items = listOf(
        BottomNavItem(
            title = "Home",
            icon = Icons.Filled.Home,
            unselected = Icons.Outlined.Home,
            news = false
        ),
        BottomNavItem(
            title = "Chats",
            icon = Icons.Filled.Email,
            unselected = Icons.Outlined.Email,
            news = false,
            badgeCount = 45
        ),
        BottomNavItem(
            title = "Settings",
            icon = Icons.Default.Settings,
            unselected = Icons.Outlined.Settings,
            news = true
        )
    )

    NavigationBar {

        items.forEachIndexed { index, item ->

            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    changeScreen(index)
                },
                label = {
                    Text(item.title)
                },
                alwaysShowLabel = false,
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount != null) {
                                Badge {
                                    Text(item.badgeCount.toString())
                                }
                            } else if (item.news) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (index == selectedItemIndex) {
                                item.icon
                            } else {
                                item.unselected
                            }, contentDescription = item.title
                        )
                    }
                }
            )
        }
    }

}


