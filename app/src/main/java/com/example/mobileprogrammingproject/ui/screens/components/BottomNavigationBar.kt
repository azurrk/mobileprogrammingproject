package com.example.mobileprogrammingproject.ui.screens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Transactions : BottomNavItem("transactions", Icons.Default.List, "Transactions")

}

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Transactions
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { onItemClick(item.route) }
            )
        }
    }
}

fun shouldShowBottomBar(route: String?): Boolean {
    return route != null && (
            route.contains("Home") || route.contains("Transaction")
            )
}