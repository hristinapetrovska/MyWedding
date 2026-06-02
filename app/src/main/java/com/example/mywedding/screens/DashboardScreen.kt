package com.example.mywedding.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mywedding.AppLanguage
import com.example.mywedding.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

enum class DashboardPage {
    HOME, TODOS, GUESTS, BUDGET, VENDORS, SCHEDULE, NOTES, SETTINGS
}

@Composable
fun DashboardScreen(
    language: AppLanguage,
    brideName: String,
    groomName: String,
    weddingDate: String
) {
    var currentPage by remember { mutableStateOf(DashboardPage.HOME) }
    var menuOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        when (currentPage) {
            DashboardPage.HOME -> DashboardHome(
                language = language,
                brideName = brideName,
                groomName = groomName,
                weddingDate = weddingDate,
                onMenuClick = { menuOpen = true },
                onPageClick = { currentPage = it }
            )

            DashboardPage.TODOS -> SimplePage("To-dos", Icons.Filled.Checklist) { currentPage = DashboardPage.HOME }
            DashboardPage.GUESTS -> SimplePage("Guests", Icons.Filled.Groups) { currentPage = DashboardPage.HOME }
            DashboardPage.BUDGET -> SimplePage("Budget", Icons.Filled.AttachMoney) { currentPage = DashboardPage.HOME }
            DashboardPage.VENDORS -> SimplePage("Vendors", Icons.Filled.Storefront) { currentPage = DashboardPage.HOME }
            DashboardPage.SCHEDULE -> SimplePage("Schedule", Icons.Filled.Event) { currentPage = DashboardPage.HOME }
            DashboardPage.NOTES -> SimplePage("Notes", Icons.Filled.StickyNote2) { currentPage = DashboardPage.HOME }
            DashboardPage.SETTINGS -> SimplePage("Settings", Icons.Filled.Settings) { currentPage = DashboardPage.HOME }
        }

        if (menuOpen) {
            SideMenu(
                language = language,
                brideName = brideName,
                groomName = groomName,
                weddingDate = weddingDate,
                onClose = { menuOpen = false },
                onPageClick = {
                    currentPage = it
                    menuOpen = false
                }
            )
        }
    }
}

@Composable
fun DashboardHome(
    language: AppLanguage,
    brideName: String,
    groomName: String,
    weddingDate: String,
    onMenuClick: () -> Unit,
    onPageClick: (DashboardPage) -> Unit
) {
    val daysLeft = calculateDaysLeft(weddingDate)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7F3))
            .padding(horizontal = 22.dp, vertical = 18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = null,
                tint = Color(0xFF2F3D40),
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onMenuClick() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "MyWedding",
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFE95D7E),
                fontFamily = FontFamily.Serif
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Filled.NotificationsNone,
                contentDescription = null,
                tint = Color(0xFF8F4F5F),
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Hello," else "Здраво,",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F3D40)
                )

                Text(
                    text = "$brideName & $groomName",
                    fontSize = 18.sp,
                    color = Color(0xFF5F4B51)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        tint = Color(0xFFE95D7E),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = weddingDate,
                        fontSize = 15.sp,
                        color = Color(0xFF5F4B51)
                    )
                }
            }

            ProgressCircle(progress = 0.65f)
        }

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE1E8))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(R.drawable.dashboardd),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 22.dp)
                ) {
                    Text(
                        text = daysLeft.toString(),
                        fontSize = 46.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFE95D7E)
                    )

                    Text(
                        text = if (language == AppLanguage.ENGLISH) "days left" else "дена остануваат",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2F3D40)
                    )

                    Text(
                        text = if (language == AppLanguage.ENGLISH) "until your wedding" else "до вашата свадба",
                        fontSize = 14.sp,
                        color = Color(0xFF7C6F73)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            DashboardCard(
                icon = Icons.Filled.Checklist,
                title = if (language == AppLanguage.ENGLISH) "To-dos" else "Задачи",
                subtitle = "0 tasks",
                modifier = Modifier.weight(1f),
                onClick = { onPageClick(DashboardPage.TODOS) }
            )

            Spacer(modifier = Modifier.width(12.dp))

            DashboardCard(
                icon = Icons.Filled.Groups,
                title = if (language == AppLanguage.ENGLISH) "Guests" else "Гости",
                subtitle = "0 invited",
                modifier = Modifier.weight(1f),
                onClick = { onPageClick(DashboardPage.GUESTS) }
            )

            Spacer(modifier = Modifier.width(12.dp))

            DashboardCard(
                icon = Icons.Filled.AttachMoney,
                title = if (language == AppLanguage.ENGLISH) "Budget" else "Буџет",
                subtitle = "0 ден",
                modifier = Modifier.weight(1f),
                onClick = { onPageClick(DashboardPage.BUDGET) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            DashboardCard(
                icon = Icons.Filled.Storefront,
                title = if (language == AppLanguage.ENGLISH) "Vendors" else "Добавувачи",
                subtitle = "0 booked",
                modifier = Modifier.weight(1f),
                onClick = { onPageClick(DashboardPage.VENDORS) }
            )

            Spacer(modifier = Modifier.width(12.dp))

            DashboardCard(
                icon = Icons.Filled.Event,
                title = if (language == AppLanguage.ENGLISH) "Schedule" else "Распоред",
                subtitle = "0 events",
                modifier = Modifier.weight(1f),
                onClick = { onPageClick(DashboardPage.SCHEDULE) }
            )

            Spacer(modifier = Modifier.width(12.dp))

            DashboardCard(
                icon = Icons.Filled.StickyNote2,
                title = if (language == AppLanguage.ENGLISH) "Notes" else "Белешки",
                subtitle = "0 notes",
                modifier = Modifier.weight(1f),
                onClick = { onPageClick(DashboardPage.NOTES) }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(language = language, onPageClick = onPageClick)
    }
}

@Composable
fun DashboardCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(116.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFE95D7E),
                modifier = Modifier.size(34.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2F3D40)
            )

            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = Color(0xFF8F4F5F)
            )
        }
    }
}

@Composable
fun ProgressCircle(progress: Float) {
    val percent = (progress * 100).toInt()

    Box(
        modifier = Modifier.size(86.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = Color(0xFFFFD7DF),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 10f, cap = StrokeCap.Round),
                size = Size(size.width, size.height),
                topLeft = Offset.Zero
            )

            drawArc(
                color = Color(0xFFE95D7E),
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 10f, cap = StrokeCap.Round),
                size = Size(size.width, size.height),
                topLeft = Offset.Zero
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$percent%",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2F3D40)
            )
            Text(
                text = "Completed",
                fontSize = 9.sp,
                color = Color(0xFF7C6F73)
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
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

            BottomNavItem(Icons.Filled.MoreHoriz, if (language == AppLanguage.ENGLISH) "More" else "Повеќе") {
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
            tint = Color(0xFF8F4F5F),
            modifier = Modifier.size(23.dp)
        )

        Text(
            text = title,
            fontSize = 9.sp,
            color = Color(0xFF8F4F5F)
        )
    }
}

@Composable
fun SimplePage(
    title: String,
    icon: ImageVector,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7F3))
            .padding(24.dp)
    ) {
        Text(
            text = "←",
            fontSize = 32.sp,
            color = Color(0xFF8F4F5F),
            modifier = Modifier.clickable { onBackClick() }
        )

        Spacer(modifier = Modifier.height(30.dp))

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFE95D7E),
            modifier = Modifier.size(70.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2F3D40)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "This section will be completed later.",
            fontSize = 16.sp,
            color = Color(0xFF7C6F73)
        )
    }
}

fun calculateDaysLeft(weddingDateText: String): Long {
    return try {
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val weddingDate = formatter.parse(weddingDateText)
        val today = Date()

        if (weddingDate != null) {
            val diff = weddingDate.time - today.time
            TimeUnit.MILLISECONDS.toDays(diff).coerceAtLeast(0)
        } else {
            0
        }
    } catch (e: Exception) {
        0
    }
}