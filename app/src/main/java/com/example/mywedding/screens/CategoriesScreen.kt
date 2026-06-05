package com.example.mywedding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mywedding.AppLanguage
import com.example.mywedding.data.DatabaseProvider
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CategoriesScreen(
    language: AppLanguage,
    onBackClick: () -> Unit,
    onCategoryClick: (String) -> Unit
) {
    val context = LocalContext.current
    val taskDao = remember { DatabaseProvider.getDatabase(context).taskDao() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"

    val tasks by taskDao.getAllTasks(userId).collectAsState(initial = emptyList())

    val categories = taskCategories

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
                text = if (language == AppLanguage.ENGLISH) "Categories" else "Категории",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2F3D40)
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(26.dp))
        }

        Spacer(modifier = Modifier.height(22.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0xFFFFE1E8), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Category,
                        contentDescription = null,
                        tint = Color(0xFFE95D7E)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = if (language == AppLanguage.ENGLISH)
                            "Task categories"
                        else
                            "Категории на задачи",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2F3D40)
                    )

                    Text(
                        text = if (language == AppLanguage.ENGLISH)
                            "Open tasks by category"
                        else
                            "Отвори задачи по категорија",
                        fontSize = 13.sp,
                        color = Color(0xFF8F4F5F)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        LazyColumn {
            items(categories) { category ->
                val count = tasks.count {
                    it.categoryEn == category.en || it.categoryMk == category.mk
                }

                CategoryCard(
                    title = if (language == AppLanguage.ENGLISH) category.en else category.mk,
                    count = count,
                    language = language,
                    onClick = {
                        onCategoryClick(category.en)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun CategoryCard(
    title: String,
    count: Int,
    language: AppLanguage,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFFFFE1E8), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Checklist,
                    contentDescription = null,
                    tint = Color(0xFFE95D7E)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F3D40)
                )

                Text(
                    text = if (language == AppLanguage.ENGLISH)
                        "$count tasks"
                    else
                        "$count задачи",
                    fontSize = 12.sp,
                    color = Color(0xFF8F4F5F)
                )
            }
        }
    }
}