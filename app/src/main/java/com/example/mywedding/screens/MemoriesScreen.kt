package com.example.mywedding.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mywedding.AppLanguage
import com.example.mywedding.data.DatabaseProvider
import com.example.mywedding.data.MemoryEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MemoriesScreen(
    language: AppLanguage,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val memoryDao = remember {
        DatabaseProvider.getDatabase(context).memoryDao()
    }

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"

    val memories by memoryDao
        .getAllMemories(userId)
        .collectAsState(initial = emptyList())

    val imagePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->

            uri?.let {

                scope.launch(Dispatchers.IO) {

                    memoryDao.insertMemory(
                        MemoryEntity(
                            userId = userId,
                            imageUri = uri.toString()
                        )
                    )
                }
            }
        }

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

            IconButton(
                onClick = onBackClick
            ) {
                Icon(Icons.Filled.ArrowBack, null)
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = if (language == AppLanguage.ENGLISH)
                    "Memories"
                else
                    "Спомени",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    imagePicker.launch("image/*")
                }
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = null,
                    tint = Color(0xFFE95D7E)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (memories.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        Icons.Filled.PhotoLibrary,
                        contentDescription = null,
                        tint = Color(0xFFE95D7E),
                        modifier = Modifier.size(60.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        if (language == AppLanguage.ENGLISH)
                            "No memories yet"
                        else
                            "Нема додадени спомени"
                    )
                }
            }

        } else {

            LazyColumn {

                items(memories, key = { it.id }) { memory ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {

                        Column {

                            Image(
                                painter = rememberAsyncImagePainter(memory.imageUri),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 20.dp,
                                            topEnd = 20.dp
                                        )
                                    ),
                                contentScale = ContentScale.Crop
                            )

                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Spacer(modifier = Modifier.weight(1f))

                                IconButton(
                                    onClick = {

                                        scope.launch(Dispatchers.IO) {
                                            memoryDao.deleteMemory(memory)
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

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}