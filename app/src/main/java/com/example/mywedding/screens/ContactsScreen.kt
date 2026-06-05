package com.example.mywedding.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
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
import com.example.mywedding.data.ContactEntity
import com.example.mywedding.data.DatabaseProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val contactCategories = listOf(
    "DJ",
    "Фотограф",
    "Видеограф",
    "Шминкер",
    "Фризер",
    "Сала",
    "Декорација",
    "Торта",
    "Цвеќара",
    "Музика",
    "Превоз"
)

@Composable
fun ContactsScreen(
    language: AppLanguage,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val contactDao = remember {
        DatabaseProvider.getDatabase(context).contactDao()
    }

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"

    val contacts by contactDao
        .getAllContacts(userId)
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
                text = "Контакти",
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

        Spacer(modifier = Modifier.height(18.dp))

        LazyColumn {

            items(contacts, key = { it.id }) { contact ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    Color(0xFFFFE1E8),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = null,
                                tint = Color(0xFFE95D7E)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = contact.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )

                            Text(
                                text = contact.category,
                                fontSize = 12.sp,
                                color = Color(0xFF8F4F5F)
                            )

                            if (contact.phone.isNotBlank()) {
                                Text(
                                    text = contact.phone,
                                    fontSize = 12.sp,
                                    color = Color(0xFF8F4F5F)
                                )
                            }

                            if (contact.note.isNotBlank()) {
                                Text(
                                    text = contact.note,
                                    fontSize = 12.sp,
                                    color = Color(0xFF8F4F5F)
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                if (contact.phone.isNotBlank()) {
                                    val intent = Intent(
                                        Intent.ACTION_DIAL,
                                        Uri.parse("tel:${contact.phone}")
                                    )
                                    context.startActivity(intent)
                                }
                            }
                        ) {
                            Icon(
                                Icons.Filled.Call,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50)
                            )
                        }

                        IconButton(
                            onClick = {
                                scope.launch(Dispatchers.IO) {
                                    contactDao.deleteContact(contact)
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

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    if (showDialog) {

        var name by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var note by remember { mutableStateOf("") }

        var selectedCategory by remember {
            mutableStateOf(contactCategories.first())
        }

        var expanded by remember {
            mutableStateOf(false)
        }

        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },

            title = {
                Text("Нов контакт")
            },

            text = {

                Column {

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Име") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box {

                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Категорија") }
                        )

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable {
                                    expanded = true
                                }
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {

                            contactCategories.forEach {

                                DropdownMenuItem(
                                    text = {
                                        Text(it)
                                    },
                                    onClick = {
                                        selectedCategory = it
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Телефон") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Белешка") }
                    )
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        if (name.isNotBlank()) {

                            scope.launch(Dispatchers.IO) {

                                contactDao.insertContact(
                                    ContactEntity(
                                        userId = userId,
                                        name = name,
                                        category = selectedCategory,
                                        phone = phone,
                                        note = note
                                    )
                                )
                            }

                            showDialog = false
                        }
                    }
                ) {
                    Text("Зачувај")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("Откажи")
                }
            }
        )
    }
}


