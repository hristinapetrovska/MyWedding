package com.example.mywedding.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    HOME, TODOS, GUESTS, BUDGET, RESTAURANTS, SEATING, GIFTS, SCHEDULE, NOTES, MEMORIES, SETTINGS
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

            DashboardPage.TODOS -> TodoScreen(
                language = language,
                onBackClick = {
                    currentPage = DashboardPage.HOME
                }
            )
            DashboardPage.GUESTS -> SimplePage("Guests", Icons.Filled.Groups) { currentPage = DashboardPage.HOME }
            DashboardPage.BUDGET -> SimplePage("Budget", Icons.Filled.AttachMoney) { currentPage = DashboardPage.HOME }
            DashboardPage.RESTAURANTS -> SimplePage("Restaurants", Icons.Filled.Storefront) { currentPage = DashboardPage.HOME }
            DashboardPage.SEATING -> SimplePage("Seating Plan", Icons.Filled.EventSeat) { currentPage = DashboardPage.HOME }
            DashboardPage.GIFTS -> SimplePage("Gift Registry", Icons.Filled.CardGiftcard) { currentPage = DashboardPage.HOME }
            DashboardPage.SCHEDULE -> SimplePage("Schedule", Icons.Filled.Event) { currentPage = DashboardPage.HOME }
            DashboardPage.NOTES -> SimplePage("Notes", Icons.Filled.StickyNote2) { currentPage = DashboardPage.HOME }
            DashboardPage.MEMORIES -> SimplePage("Memories", Icons.Filled.PhotoLibrary) { currentPage = DashboardPage.HOME }
            DashboardPage.SETTINGS -> SimplePage("Settings", Icons.Filled.Settings) { currentPage = DashboardPage.HOME }
        }

        if (menuOpen) {
            SideMenu (
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 22.dp, end = 22.dp, bottom = 18.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            BottomNavBar(
                language = language,
                onPageClick = { currentPage = it }
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

    val totalTasks = defaultWeddingTasks().size
    val guestsCount = 0
    val budgetAmount = 0
    val selectedRestaurants = 0
    val tablesCount = 0
    val giftsCount = 0
    val eventsCount = 0
    val notesCount = 0
    val memoriesCount = 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7F3))
            .padding(horizontal = 22.dp, vertical = 18.dp)
    ) {
        TopDashboardBar(onMenuClick = onMenuClick)

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

            ProgressCircle(progress = 0f)
        }

        Spacer(modifier = Modifier.height(18.dp))

        CountdownCard(
            language = language,
            daysLeft = daysLeft
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            DashboardCard(
                Icons.Filled.Checklist,
                if (language == AppLanguage.ENGLISH) "To-dos" else "Задачи",
                if (language == AppLanguage.ENGLISH) "$totalTasks tasks" else "$totalTasks задачи",
                Modifier.weight(1f)
            ) {
                onPageClick(DashboardPage.TODOS)
            }

            Spacer(modifier = Modifier.width(12.dp))

            DashboardCard(
                Icons.Filled.Groups,
                if (language == AppLanguage.ENGLISH) "Guests" else "Гости",
                if (language == AppLanguage.ENGLISH) "$guestsCount invited" else "$guestsCount гости",
                Modifier.weight(1f)
            ) {
                onPageClick(DashboardPage.GUESTS)
            }

            Spacer(modifier = Modifier.width(12.dp))

            DashboardCard(
                Icons.Filled.AttachMoney,
                if (language == AppLanguage.ENGLISH) "Budget" else "Буџет",
                "$budgetAmount ден",
                Modifier.weight(1f)
            ) {
                onPageClick(DashboardPage.BUDGET)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            DashboardCard(
                Icons.Filled.Storefront,
                if (language == AppLanguage.ENGLISH) "Restaurants" else "Ресторани",
                if (language == AppLanguage.ENGLISH) "$selectedRestaurants selected" else "$selectedRestaurants избрани",
                Modifier.weight(1f)
            ) {
                onPageClick(DashboardPage.RESTAURANTS)
            }

            Spacer(modifier = Modifier.width(12.dp))

            DashboardCard(
                Icons.Filled.EventSeat,
                if (language == AppLanguage.ENGLISH) "Seating" else "Маси",
                if (language == AppLanguage.ENGLISH) "$tablesCount tables" else "$tablesCount маси",
                Modifier.weight(1f)
            ) {
                onPageClick(DashboardPage.SEATING)
            }

            Spacer(modifier = Modifier.width(12.dp))

            DashboardCard(
                Icons.Filled.CardGiftcard,
                if (language == AppLanguage.ENGLISH) "Gifts" else "Подароци",
                if (language == AppLanguage.ENGLISH) "$giftsCount gifts" else "$giftsCount подароци",
                Modifier.weight(1f)
            ) {
                onPageClick(DashboardPage.GIFTS)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            DashboardCard(
                Icons.Filled.Event,
                if (language == AppLanguage.ENGLISH) "Schedule" else "План",
                if (language == AppLanguage.ENGLISH) "$eventsCount events" else "$eventsCount настани",
                Modifier.weight(1f)
            ) {
                onPageClick(DashboardPage.SCHEDULE)
            }

            Spacer(modifier = Modifier.width(12.dp))

            DashboardCard(
                Icons.Filled.StickyNote2,
                if (language == AppLanguage.ENGLISH) "Notes" else "Белешки",
                if (language == AppLanguage.ENGLISH) "$notesCount notes" else "$notesCount белешки",
                Modifier.weight(1f)
            ) {
                onPageClick(DashboardPage.NOTES)
            }

            Spacer(modifier = Modifier.width(12.dp))

            DashboardCard(
                Icons.Filled.PhotoLibrary,
                if (language == AppLanguage.ENGLISH) "Memories" else "Спомени",
                if (language == AppLanguage.ENGLISH) "$memoriesCount photos" else "$memoriesCount слики",
                Modifier.weight(1f)
            ) {
                onPageClick(DashboardPage.MEMORIES)
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun TopDashboardBar(onMenuClick: () -> Unit) {
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
}

@Composable
fun CountdownCard(
    language: AppLanguage,
    daysLeft: Long
) {
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