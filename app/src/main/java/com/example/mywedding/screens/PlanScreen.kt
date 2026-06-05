package com.example.mywedding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.StickyNote2
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
import com.example.mywedding.data.PlanEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PlanScreen(
    language: AppLanguage,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val planDao = remember {
        DatabaseProvider.getDatabase(context).planDao()
    }

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"

    val plans by planDao
        .getAllPlans(userId)
        .collectAsState(initial = emptyList())

    var showDialog by remember { mutableStateOf(false) }

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
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onBackClick() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = if (language == AppLanguage.ENGLISH)
                    "Wedding Plan"
                else
                    "План",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = Color(0xFFE95D7E),
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        showDialog = true
                    }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (plans.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {

                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        Icons.Filled.AccessTime,
                        contentDescription = null,
                        tint = Color(0xFFE95D7E),
                        modifier = Modifier.size(54.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        if (language == AppLanguage.ENGLISH)
                            "No events added"
                        else
                            "Нема додадени настани"
                    )
                }
            }

        } else {

            LazyColumn {

                items(plans, key = { it.id }) { plan ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = plan.time,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE95D7E)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = plan.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2F3D40)
                            )

                            if (plan.location.isNotBlank()) {

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Icon(
                                        Icons.Filled.LocationOn,
                                        contentDescription = null,
                                        tint = Color(0xFF8F4F5F),
                                        modifier = Modifier.size(16.dp)
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Text(
                                        plan.location,
                                        color = Color(0xFF8F4F5F)
                                    )
                                }
                            }

                            if (plan.note.isNotBlank()) {

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    plan.note,
                                    color = Color(0xFF8F4F5F)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row {

                                Spacer(modifier = Modifier.weight(1f))

                                IconButton(
                                    onClick = {
                                        scope.launch(Dispatchers.IO) {
                                            planDao.deletePlan(plan)
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = null,
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }

    if (showDialog) {

        var time by remember { mutableStateOf("") }
        var title by remember { mutableStateOf("") }
        var location by remember { mutableStateOf("") }
        var note by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },

            title = {
                Text(
                    if (language == AppLanguage.ENGLISH)
                        "New Event"
                    else
                        "Нов Настан"
                )
            },

            text = {

                Column {

                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = {
                            Text(
                                if (language == AppLanguage.ENGLISH)
                                    "Time"
                                else
                                    "Време"
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = {
                            Text(
                                if (language == AppLanguage.ENGLISH)
                                    "Event"
                                else
                                    "Настан"
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = {
                            Text(
                                if (language == AppLanguage.ENGLISH)
                                    "Location"
                                else
                                    "Локација"
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = {
                            Text(
                                if (language == AppLanguage.ENGLISH)
                                    "Note"
                                else
                                    "Белешка"
                            )
                        }
                    )
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        if (title.isNotBlank()) {

                            scope.launch(Dispatchers.IO) {

                                planDao.insertPlan(
                                    PlanEntity(
                                        userId = userId,
                                        time = time,
                                        title = title,
                                        location = location,
                                        note = note
                                    )
                                )
                            }

                            showDialog = false
                        }
                    }
                ) {
                    Text(
                        if (language == AppLanguage.ENGLISH)
                            "Save"
                        else
                            "Зачувај"
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text(
                        if (language == AppLanguage.ENGLISH)
                            "Cancel"
                        else
                            "Откажи"
                    )
                }
            }
        )
    }
}