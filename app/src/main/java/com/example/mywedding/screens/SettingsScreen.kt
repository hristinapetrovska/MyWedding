package com.example.mywedding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mywedding.AppLanguage

@Composable
fun SettingsScreen(
    language: AppLanguage,
    brideName: String,
    groomName: String,
    weddingDate: String,
    onBackClick: () -> Unit,
    onSaveWeddingData: (String, String, String) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onLogout: () -> Unit
) {
    var bride by remember { mutableStateOf(brideName) }
    var groom by remember { mutableStateOf(groomName) }
    var date by remember { mutableStateOf(weddingDate) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7F3))
            .padding(start = 22.dp, end = 22.dp, top = 22.dp, bottom = 90.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                tint = Color(0xFF2F3D40),
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onBackClick() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = if (language == AppLanguage.ENGLISH) "Settings" else "Поставки",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2F3D40)
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(26.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        SettingsCardTitle(
            icon = Icons.Filled.Person,
            title = if (language == AppLanguage.ENGLISH) "Wedding details" else "Свадбени податоци"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                OutlinedTextField(
                    value = bride,
                    onValueChange = { bride = it },
                    label = { Text(if (language == AppLanguage.ENGLISH) "Bride name" else "Име на невеста") },
                    leadingIcon = { Icon(Icons.Filled.Person, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = groom,
                    onValueChange = { groom = it },
                    label = { Text(if (language == AppLanguage.ENGLISH) "Groom name" else "Име на младоженец") },
                    leadingIcon = { Icon(Icons.Filled.Person, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text(if (language == AppLanguage.ENGLISH) "Wedding date" else "Датум на свадба") },
                    leadingIcon = { Icon(Icons.Filled.CalendarMonth, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (bride.isNotBlank() && groom.isNotBlank() && date.isNotBlank()) {
                            onSaveWeddingData(bride, groom, date)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE95D7E))
                ) {
                    Icon(Icons.Filled.Save, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (language == AppLanguage.ENGLISH) "Save changes" else "Зачувај промени")
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        SettingsCardTitle(
            icon = Icons.Filled.Language,
            title = if (language == AppLanguage.ENGLISH) "Language" else "Јазик"
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LanguageButton(
                    text = "Македонски",
                    selected = language == AppLanguage.MACEDONIAN,
                    modifier = Modifier.weight(1f)
                ) {
                    onLanguageChange(AppLanguage.MACEDONIAN)
                }

                Spacer(modifier = Modifier.width(12.dp))

                LanguageButton(
                    text = "English",
                    selected = language == AppLanguage.ENGLISH,
                    modifier = Modifier.weight(1f)
                ) {
                    onLanguageChange(AppLanguage.ENGLISH)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))
        ) {
            Icon(Icons.Filled.Logout, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (language == AppLanguage.ENGLISH) "Logout" else "Одјави се")
        }
    }
}

@Composable
fun SettingsCardTitle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(Color(0xFFFFE1E8), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFE95D7E),
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2F3D40)
        )
    }
}

@Composable
fun LanguageButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                if (selected) Color(0xFFE95D7E) else Color(0xFFFFF7F3),
                RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) Color.White else Color(0xFF5F4B51)
        )
    }
}