package com.example.mywedding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.mywedding.data.GuestEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class GuestFilter {
    ALL, BRIDE, GROOM
}

enum class GuestStatus {
    WAITING, CONFIRMED, DECLINED
}

@Composable
fun GuestsScreen(
    language: AppLanguage,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val guestDao = remember { DatabaseProvider.getDatabase(context).guestDao() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"

    val guests by guestDao.getAllGuests(userId).collectAsState(initial = emptyList())

    var selectedFilter by remember { mutableStateOf(GuestFilter.ALL) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedGuest by remember { mutableStateOf<GuestEntity?>(null) }

    val confirmedCount = guests.count { it.status == GuestStatus.CONFIRMED.name }
    val waitingCount = guests.count { it.status == GuestStatus.WAITING.name }
    val declinedCount = guests.count { it.status == GuestStatus.DECLINED.name }

    val filteredGuests = when (selectedFilter) {
        GuestFilter.BRIDE -> guests.filter { it.side == "BRIDE" }
        GuestFilter.GROOM -> guests.filter { it.side == "GROOM" }
        else -> guests
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7F3))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                    text = if (language == AppLanguage.ENGLISH) "Guests" else "Гости",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F3D40)
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = Color(0xFFE95D7E),
                    modifier = Modifier
                        .size(26.dp)
                        .clickable {
                            selectedGuest = null
                            showDialog = true
                        }
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                GuestStatCard(
                    title = if (language == AppLanguage.ENGLISH) "Total" else "Вкупно",
                    count = guests.size,
                    color = Color(0xFFE3F2FD),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                GuestStatCard(
                    title = if (language == AppLanguage.ENGLISH) "Confirmed" else "Потврдени",
                    count = confirmedCount,
                    color = Color(0xFFE2F8E8),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                GuestStatCard(
                    title = if (language == AppLanguage.ENGLISH) "Waiting" else "Се чека",
                    count = waitingCount,
                    color = Color(0xFFFFF1D6),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                GuestStatCard(
                    title = if (language == AppLanguage.ENGLISH) "Declined" else "Одбиени",
                    count = declinedCount,
                    color = Color(0xFFFFE1E8),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                GuestFilterButton(
                    text = if (language == AppLanguage.ENGLISH) "All" else "Сите",
                    selected = selectedFilter == GuestFilter.ALL
                ) {
                    selectedFilter = GuestFilter.ALL
                }

                Spacer(modifier = Modifier.width(8.dp))

                GuestFilterButton(
                    text = if (language == AppLanguage.ENGLISH) "Bride side" else "Невеста",
                    selected = selectedFilter == GuestFilter.BRIDE
                ) {
                    selectedFilter = GuestFilter.BRIDE
                }

                Spacer(modifier = Modifier.width(8.dp))

                GuestFilterButton(
                    text = if (language == AppLanguage.ENGLISH) "Groom side" else "Младоженец",
                    selected = selectedFilter == GuestFilter.GROOM
                ) {
                    selectedFilter = GuestFilter.GROOM
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(filteredGuests, key = { it.id }) { guest ->
                    GuestCard(
                        guest = guest,
                        language = language,
                        onClick = {
                            selectedGuest = guest
                            showDialog = true
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        FloatingActionButton(
            onClick = {
                selectedGuest = null
                showDialog = true
            },
            containerColor = Color(0xFFE95D7E),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
        }

        if (showDialog) {
            GuestDialog(
                language = language,
                guest = selectedGuest,
                userId = userId,
                onDismiss = { showDialog = false },
                onSave = { guest ->
                    scope.launch(Dispatchers.IO) {
                        if (guest.id == 0) {
                            guestDao.insertGuest(guest)
                        } else {
                            guestDao.updateGuest(guest)
                        }
                    }
                    showDialog = false
                },
                onDelete = { guest ->
                    scope.launch(Dispatchers.IO) {
                        guestDao.deleteGuest(guest)
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun GuestStatCard(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = count.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2F3D40)
            )

            Text(
                text = title,
                fontSize = 9.sp,
                color = Color(0xFF5F4B51)
            )
        }
    }
}

@Composable
fun GuestFilterButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (selected) Color(0xFFE95D7E) else Color.White,
                shape = RoundedCornerShape(50)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 9.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) Color.White else Color(0xFF5F4B51)
        )
    }
}

@Composable
fun GuestCard(
    guest: GuestEntity,
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
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFFFFE1E8), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color(0xFFE95D7E)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = guest.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F3D40)
                )

                Text(
                    text = if (guest.side == "BRIDE")
                        if (language == AppLanguage.ENGLISH) "Bride side" else "Страна на невеста"
                    else
                        if (language == AppLanguage.ENGLISH) "Groom side" else "Страна на младоженец",
                    fontSize = 12.sp,
                    color = Color(0xFF8F4F5F)
                )

                if (guest.phone.isNotBlank()) {
                    Text(
                        text = guest.phone,
                        fontSize = 12.sp,
                        color = Color(0xFF8F4F5F)
                    )
                }
            }

            GuestStatusBadge(status = guest.status, language = language)
        }
    }
}

@Composable
fun GuestStatusBadge(
    status: String,
    language: AppLanguage
) {
    val text = when (status) {
        GuestStatus.CONFIRMED.name -> if (language == AppLanguage.ENGLISH) "Confirmed" else "Потврден"
        GuestStatus.DECLINED.name -> if (language == AppLanguage.ENGLISH) "Declined" else "Одбиен"
        else -> if (language == AppLanguage.ENGLISH) "Waiting" else "Се чека"
    }

    val bgColor = when (status) {
        GuestStatus.CONFIRMED.name -> Color(0xFFE2F8E8)
        GuestStatus.DECLINED.name -> Color(0xFFFFE1E8)
        else -> Color(0xFFFFF1D6)
    }

    val textColor = when (status) {
        GuestStatus.CONFIRMED.name -> Color(0xFF4CAF50)
        GuestStatus.DECLINED.name -> Color(0xFFE95D7E)
        else -> Color(0xFFF2994A)
    }

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun GuestDialog(
    language: AppLanguage,
    guest: GuestEntity?,
    userId: String,
    onDismiss: () -> Unit,
    onSave: (GuestEntity) -> Unit,
    onDelete: (GuestEntity) -> Unit
) {
    val isEditing = guest != null

    var name by remember { mutableStateOf(guest?.name ?: "") }
    var phone by remember { mutableStateOf(guest?.phone ?: "") }
    var side by remember { mutableStateOf(guest?.side ?: "BRIDE") }
    var status by remember { mutableStateOf(guest?.status ?: GuestStatus.WAITING.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEditing)
                    if (language == AppLanguage.ENGLISH) "Edit guest" else "Измени гостин"
                else
                    if (language == AppLanguage.ENGLISH) "Add guest" else "Додај гостин"
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(if (language == AppLanguage.ENGLISH) "Guest name" else "Име на гостин") },
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(if (language == AppLanguage.ENGLISH) "Phone" else "Телефон") },
                    leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (language == AppLanguage.ENGLISH) "Guest side" else "Страна на гостин",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F3D40)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    GuestSmallButton(
                        text = if (language == AppLanguage.ENGLISH) "Bride" else "Невеста",
                        selected = side == "BRIDE"
                    ) {
                        side = "BRIDE"
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    GuestSmallButton(
                        text = if (language == AppLanguage.ENGLISH) "Groom" else "Младоженец",
                        selected = side == "GROOM"
                    ) {
                        side = "GROOM"
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (language == AppLanguage.ENGLISH) "Attendance status" else "Статус на присуство",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F3D40)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    GuestSmallButton(
                        text = if (language == AppLanguage.ENGLISH) "Waiting" else "Се чека",
                        selected = status == GuestStatus.WAITING.name
                    ) {
                        status = GuestStatus.WAITING.name
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    GuestSmallButton(
                        text = if (language == AppLanguage.ENGLISH) "Confirmed" else "Потврден",
                        selected = status == GuestStatus.CONFIRMED.name
                    ) {
                        status = GuestStatus.CONFIRMED.name
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                GuestSmallButton(
                    text = if (language == AppLanguage.ENGLISH) "Declined" else "Одбиен",
                    selected = status == GuestStatus.DECLINED.name
                ) {
                    status = GuestStatus.DECLINED.name
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(
                            GuestEntity(
                                id = guest?.id ?: 0,
                                userId = userId,
                                name = name,
                                phone = phone,
                                side = side,
                                status = status
                            )
                        )
                    }
                }
            ) {
                Text(if (language == AppLanguage.ENGLISH) "Save" else "Зачувај")
            }
        },
        dismissButton = {
            Row {
                if (isEditing) {
                    TextButton(onClick = { onDelete(guest!!) }) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Delete" else "Избриши",
                            color = Color(0xFFB00020)
                        )
                    }
                }

                TextButton(onClick = onDismiss) {
                    Text(if (language == AppLanguage.ENGLISH) "Cancel" else "Откажи")
                }
            }
        }
    )
}

@Composable
fun GuestSmallButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                if (selected) Color(0xFFE95D7E) else Color(0xFFFFF7F3),
                RoundedCornerShape(50)
            )
            .clickable { onClick() }
            .padding(horizontal = 11.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            color = if (selected) Color.White else Color(0xFF5F4B51),
            fontWeight = FontWeight.SemiBold
        )
    }
}