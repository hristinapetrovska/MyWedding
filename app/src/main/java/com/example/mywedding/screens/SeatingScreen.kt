package com.example.mywedding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Groups
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
import com.example.mywedding.data.TableEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class SeatingFilter {
    ALL, BRIDE, GROOM
}

@Composable
fun SeatingScreen(
    language: AppLanguage,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val database = remember { DatabaseProvider.getDatabase(context) }
    val tableDao = remember { database.tableDao() }
    val guestDao = remember { database.guestDao() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"

    val tables by tableDao.getAllTables(userId).collectAsState(initial = emptyList())
    val guests by guestDao.getAllGuests(userId).collectAsState(initial = emptyList())

    val confirmedGuests = guests.filter { it.status == GuestStatus.CONFIRMED.name }

    var showAddTableDialog by remember { mutableStateOf(false) }
    var selectedTable by remember { mutableStateOf<TableEntity?>(null) }

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
                    text = if (language == AppLanguage.ENGLISH) "Seating Plan" else "Распоред на маси",
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
                        .clickable { showAddTableDialog = true }
                )
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
                            imageVector = Icons.Filled.EventSeat,
                            contentDescription = null,
                            tint = Color(0xFFE95D7E)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = if (language == AppLanguage.ENGLISH)
                                "${tables.size} tables"
                            else
                                "${tables.size} маси",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2F3D40)
                        )

                        Text(
                            text = if (language == AppLanguage.ENGLISH)
                                "${confirmedGuests.size} confirmed guests available"
                            else
                                "${confirmedGuests.size} потврдени гости достапни",
                            fontSize = 13.sp,
                            color = Color(0xFF8F4F5F)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (tables.isEmpty()) {
                EmptySeatingCard(language = language)
            } else {
                LazyColumn {
                    items(tables, key = { it.id }) { table ->
                        val tableGuestIds = parseGuestIds(table.guestIds)
                        val tableGuests = confirmedGuests.filter { it.id in tableGuestIds }

                        SeatingTableCard(
                            language = language,
                            table = table,
                            tableGuests = tableGuests,
                            onClick = { selectedTable = table },
                            onDelete = {
                                scope.launch(Dispatchers.IO) {
                                    tableDao.deleteTable(table)
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddTableDialog = true },
            containerColor = Color(0xFFE95D7E),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
        }

        if (showAddTableDialog) {
            AddTableDialog(
                language = language,
                onDismiss = { showAddTableDialog = false },
                onSave = { tableNumber ->
                    scope.launch(Dispatchers.IO) {
                        tableDao.insertTable(
                            TableEntity(
                                userId = userId,
                                tableNumber = tableNumber
                            )
                        )
                    }
                    showAddTableDialog = false
                }
            )
        }

        selectedTable?.let { table ->
            EditTableGuestsDialog(
                language = language,
                table = table,
                confirmedGuests = confirmedGuests,
                onDismiss = { selectedTable = null },
                onSave = { updatedGuestIds ->
                    scope.launch(Dispatchers.IO) {
                        tableDao.updateTable(
                            table.copy(
                                guestIds = updatedGuestIds.joinToString(",")
                            )
                        )
                    }
                    selectedTable = null
                }
            )
        }
    }
}

@Composable
fun EmptySeatingCard(language: AppLanguage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.EventSeat,
                contentDescription = null,
                tint = Color(0xFFE95D7E),
                modifier = Modifier.size(54.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (language == AppLanguage.ENGLISH)
                    "No tables yet"
                else
                    "Нема креирани маси",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2F3D40)
            )

            Text(
                text = if (language == AppLanguage.ENGLISH)
                    "Add tables and assign confirmed guests."
                else
                    "Додај маси и распореди потврдени гости.",
                fontSize = 13.sp,
                color = Color(0xFF8F4F5F)
            )
        }
    }
}

@Composable
fun SeatingTableCard(
    language: AppLanguage,
    table: TableEntity,
    tableGuests: List<GuestEntity>,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFFFE1E8), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.EventSeat,
                    contentDescription = null,
                    tint = Color(0xFFE95D7E)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (language == AppLanguage.ENGLISH)
                        "Table ${table.tableNumber}"
                    else
                        "Маса ${table.tableNumber}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F3D40)
                )

                Text(
                    text = if (language == AppLanguage.ENGLISH)
                        "${tableGuests.size} guests assigned"
                    else
                        "${tableGuests.size} гости распоредени",
                    fontSize = 12.sp,
                    color = Color(0xFF8F4F5F)
                )

                if (tableGuests.isNotEmpty()) {
                    Text(
                        text = tableGuests.take(3).joinToString(", ") { it.name },
                        fontSize = 12.sp,
                        color = Color(0xFF8F4F5F)
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = Color(0xFFB00020)
                )
            }
        }
    }
}

@Composable
fun AddTableDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var tableNumber by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (language == AppLanguage.ENGLISH) "Add table" else "Додај маса")
        },
        text = {
            OutlinedTextField(
                value = tableNumber,
                onValueChange = { tableNumber = it.filter { char -> char.isDigit() } },
                label = { Text(if (language == AppLanguage.ENGLISH) "Table number" else "Број на маса") },
                leadingIcon = { Icon(Icons.Filled.EventSeat, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val number = tableNumber.toIntOrNull()
                    if (number != null && number > 0) {
                        onSave(number)
                    }
                }
            ) {
                Text(if (language == AppLanguage.ENGLISH) "Save" else "Зачувај")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (language == AppLanguage.ENGLISH) "Cancel" else "Откажи")
            }
        }
    )
}

@Composable
fun EditTableGuestsDialog(
    language: AppLanguage,
    table: TableEntity,
    confirmedGuests: List<GuestEntity>,
    onDismiss: () -> Unit,
    onSave: (List<Int>) -> Unit
) {
    var filter by remember { mutableStateOf(SeatingFilter.ALL) }
    var selectedIds by remember { mutableStateOf(parseGuestIds(table.guestIds)) }

    val filteredGuests = when (filter) {
        SeatingFilter.BRIDE -> confirmedGuests.filter { it.side == "BRIDE" }
        SeatingFilter.GROOM -> confirmedGuests.filter { it.side == "GROOM" }
        SeatingFilter.ALL -> confirmedGuests
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (language == AppLanguage.ENGLISH)
                    "Table ${table.tableNumber}"
                else
                    "Маса ${table.tableNumber}"
            )
        },
        text = {
            Column {
                Row {
                    SeatingFilterButton(
                        text = if (language == AppLanguage.ENGLISH) "All" else "Сите",
                        selected = filter == SeatingFilter.ALL
                    ) { filter = SeatingFilter.ALL }

                    Spacer(modifier = Modifier.width(8.dp))

                    SeatingFilterButton(
                        text = if (language == AppLanguage.ENGLISH) "Bride" else "Невеста",
                        selected = filter == SeatingFilter.BRIDE
                    ) { filter = SeatingFilter.BRIDE }

                    Spacer(modifier = Modifier.width(8.dp))

                    SeatingFilterButton(
                        text = if (language == AppLanguage.ENGLISH) "Groom" else "Младоженец",
                        selected = filter == SeatingFilter.GROOM
                    ) { filter = SeatingFilter.GROOM }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (filteredGuests.isEmpty()) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH)
                            "No confirmed guests in this category."
                        else
                            "Нема потврдени гости во оваа категорија.",
                        color = Color(0xFF8F4F5F)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 360.dp)
                    ) {
                        items(filteredGuests, key = { it.id }) { guest ->
                            val checked = guest.id in selectedIds

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedIds =
                                            if (checked) selectedIds - guest.id
                                            else selectedIds + guest.id
                                    }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = {
                                        selectedIds =
                                            if (checked) selectedIds - guest.id
                                            else selectedIds + guest.id
                                    }
                                )

                                Column {
                                    Text(
                                        text = guest.name,
                                        fontWeight = FontWeight.SemiBold,
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
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(selectedIds) }) {
                Text(if (language == AppLanguage.ENGLISH) "Save" else "Зачувај")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (language == AppLanguage.ENGLISH) "Cancel" else "Откажи")
            }
        }
    )
}

@Composable
fun SeatingFilterButton(
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
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) Color.White else Color(0xFF5F4B51)
        )
    }
}

fun parseGuestIds(ids: String): List<Int> {
    if (ids.isBlank()) return emptyList()

    return ids.split(",")
        .mapNotNull { it.trim().toIntOrNull() }
}