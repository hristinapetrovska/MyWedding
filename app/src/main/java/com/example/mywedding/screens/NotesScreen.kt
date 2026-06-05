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
import androidx.compose.material.icons.filled.Note
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
import com.example.mywedding.data.NoteEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NotesScreen(
    language: AppLanguage,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val noteDao = remember {
        DatabaseProvider.getDatabase(context).noteDao()
    }

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"

    val notes by noteDao
        .getAllNotes(userId)
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
                    "Notes"
                else
                    "Белешки",
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

        if (notes.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {

                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        Icons.Filled.Note,
                        contentDescription = null,
                        tint = Color(0xFFE95D7E),
                        modifier = Modifier.size(54.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        if (language == AppLanguage.ENGLISH)
                            "No notes yet"
                        else
                            "Нема белешки"
                    )
                }
            }

        } else {

            LazyColumn {

                items(notes, key = { it.id }) { note ->

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
                                text = note.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF2F3D40)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = note.content,
                                color = Color(0xFF8F4F5F)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row {

                                Spacer(modifier = Modifier.weight(1f))

                                IconButton(
                                    onClick = {
                                        scope.launch(Dispatchers.IO) {
                                            noteDao.deleteNote(note)
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

        var title by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },

            title = {
                Text(
                    if (language == AppLanguage.ENGLISH)
                        "New Note"
                    else
                        "Нова белешка"
                )
            },

            text = {

                Column {

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = {
                            Text(
                                if (language == AppLanguage.ENGLISH)
                                    "Title"
                                else
                                    "Наслов"
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = {
                            Text(
                                if (language == AppLanguage.ENGLISH)
                                    "Content"
                                else
                                    "Содржина"
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

                                noteDao.insertNote(
                                    NoteEntity(
                                        userId = userId,
                                        title = title,
                                        content = content
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