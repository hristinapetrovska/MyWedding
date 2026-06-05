package com.example.mywedding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
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
    onLogout: () -> Unit,
    onPageClick: (DashboardPage) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(300.dp)
                .background(Color(0xFFFFF7F3))
                .padding(20.dp)
        ) {

            Text(
                text = "MyWedding",
                fontSize = 28.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE95D7E)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "$brideName & $groomName",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2F3D40)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = weddingDate,
                        fontSize = 13.sp,
                        color = Color(0xFF8F4F5F)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            MenuItem(
                icon = Icons.Filled.Home,
                title = if (language == AppLanguage.ENGLISH)
                    "Home"
                else
                    "Дома"
            ) {
                onPageClick(DashboardPage.HOME)
            }

            MenuItem(
                icon = Icons.Filled.Checklist,
                title = if (language == AppLanguage.ENGLISH)
                    "All Tasks"
                else
                    "Сите задачи"
            ) {
                onPageClick(DashboardPage.TODOS)
            }

            MenuItem(
                icon = Icons.Filled.Category,
                title = if (language == AppLanguage.ENGLISH)
                    "Categories"
                else
                    "Категории"
            ) {
                onPageClick(DashboardPage.CATEGORIES)
            }

            MenuItem(
                icon = Icons.Filled.Contacts,
                title = if (language == AppLanguage.ENGLISH)
                    "Contacts"
                else
                    "Контакти"
            ) {
                onPageClick(DashboardPage.CONTACTS)
            }

            MenuItem(
                icon = Icons.Filled.Settings,
                title = if (language == AppLanguage.ENGLISH)
                    "Settings"
                else
                    "Поставки"
            ) {
                onPageClick(DashboardPage.SETTINGS)
            }

            Spacer(modifier = Modifier.weight(1f))

            Divider()

            Spacer(modifier = Modifier.height(10.dp))

            MenuItem(
                icon = Icons.Filled.Logout,
                title = if (language == AppLanguage.ENGLISH)
                    "Logout"
                else
                    "Одјави се",
                iconColor = Color.Red,
                textColor = Color.Red
            ) {
                onLogout()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
                .clickable {
                    onClose()
                }
        )
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
    title: String,
    iconColor: Color = Color(0xFFE95D7E),
    textColor: Color = Color(0xFF2F3D40),
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    Color(0xFFFFE1E8),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}