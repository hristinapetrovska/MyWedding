package com.example.mywedding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mywedding.AppLanguage

@Composable
fun SideMenu(
    language: AppLanguage,
    brideName: String,
    groomName: String,
    weddingDate: String,
    onClose: () -> Unit,
    onPageClick: (DashboardPage) -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(275.dp)
                .background(Color.White)
                .padding(22.dp)
        ) {
            Text(
                text = "$brideName & $groomName",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2F3D40)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = weddingDate,
                fontSize = 13.sp,
                color = Color(0xFF8F4F5F)
            )

            Spacer(modifier = Modifier.height(28.dp))

            MenuItem(Icons.Filled.Checklist, if (language == AppLanguage.ENGLISH) "All To-dos" else "Сите задачи") {
                onPageClick(DashboardPage.TODOS)
            }

            MenuItem(Icons.Filled.Groups, if (language == AppLanguage.ENGLISH) "Guests" else "Гости") {
                onPageClick(DashboardPage.GUESTS)
            }

            MenuItem(Icons.Filled.AttachMoney, if (language == AppLanguage.ENGLISH) "Budget" else "Буџет") {
                onPageClick(DashboardPage.BUDGET)
            }

            MenuItem(Icons.Filled.Storefront, if (language == AppLanguage.ENGLISH) "Vendors" else "Добавувачи") {
                onPageClick(DashboardPage.VENDORS)
            }

            MenuItem(Icons.Filled.Event, if (language == AppLanguage.ENGLISH) "Schedule" else "Распоред") {
                onPageClick(DashboardPage.SCHEDULE)
            }

            MenuItem(Icons.Filled.StickyNote2, if (language == AppLanguage.ENGLISH) "Notes" else "Белешки") {
                onPageClick(DashboardPage.NOTES)
            }

            MenuItem(Icons.Filled.Settings, if (language == AppLanguage.ENGLISH) "Settings" else "Поставки") {
                onPageClick(DashboardPage.SETTINGS)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
                .clickable { onClose() }
        )
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFE95D7E),
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = title,
            fontSize = 15.sp,
            color = Color(0xFF2F3D40)
        )
    }
}