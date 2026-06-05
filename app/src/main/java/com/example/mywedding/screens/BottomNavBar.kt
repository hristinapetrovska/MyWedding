package com.example.mywedding.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mywedding.AppLanguage

@Composable
fun BottomNavBar(
    language: AppLanguage,
    onPageClick: (DashboardPage) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 9.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomNavItem(Icons.Filled.Home, if (language == AppLanguage.ENGLISH) "Home" else "Дома") {
                onPageClick(DashboardPage.HOME)
            }

            BottomNavItem(Icons.Filled.Checklist, if (language == AppLanguage.ENGLISH) "To-dos" else "Задачи") {
                onPageClick(DashboardPage.TODOS)
            }

            BottomNavItem(Icons.Filled.Groups, if (language == AppLanguage.ENGLISH) "Guests" else "Гости") {
                onPageClick(DashboardPage.GUESTS)
            }

            BottomNavItem(Icons.Filled.AttachMoney, if (language == AppLanguage.ENGLISH) "Budget" else "Буџет") {
                onPageClick(DashboardPage.BUDGET)
            }

            BottomNavItem(Icons.Filled.StickyNote2, if (language == AppLanguage.ENGLISH) "Notes" else "Белешки") {
                onPageClick(DashboardPage.NOTES)
            }
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFB76E79),
            modifier = Modifier.size(23.dp)
        )

        Text(
            text = title,
            fontSize = 9.sp,
            color = Color(0xFFB76E79)
        )
    }
}