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
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
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
import com.example.mywedding.data.GiftEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun GiftsScreen(
    language: AppLanguage,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val giftDao = remember { DatabaseProvider.getDatabase(context).giftDao() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"

    val gifts by giftDao.getAllGifts(userId).collectAsState(initial = emptyList())

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
                tint = Color(0xFF2F3D40),
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onBackClick() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = if (language == AppLanguage.ENGLISH) "Gifts" else "Подароци",
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
                    .clickable { showDialog = true }
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
                        imageVector = Icons.Filled.CardGiftcard,
                        contentDescription = null,
                        tint = Color(0xFFE95D7E)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = if (language == AppLanguage.ENGLISH)
                            "${gifts.size} received gifts"
                        else
                            "${gifts.size} примени подароци",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2F3D40)
                    )

                    Text(
                        text = if (language == AppLanguage.ENGLISH)
                            "Keep track of wedding gifts"
                        else
                            "Евиденција на подароците од свадбата",
                        fontSize = 13.sp,
                        color = Color(0xFF8F4F5F)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (gifts.isEmpty()) {
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
                        imageVector = Icons.Filled.CardGiftcard,
                        contentDescription = null,
                        tint = Color(0xFFE95D7E),
                        modifier = Modifier.size(54.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (language == AppLanguage.ENGLISH)
                            "No gifts added yet"
                        else
                            "Нема внесени подароци",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2F3D40)
                    )

                    Text(
                        text = if (language == AppLanguage.ENGLISH)
                            "Add received wedding gifts."
                        else
                            "Додај примени свадбени подароци.",
                        fontSize = 13.sp,
                        color = Color(0xFF8F4F5F)
                    )
                }
            }
        } else {
            LazyColumn {
                items(gifts, key = { it.id }) { gift ->
                    GiftCard(
                        gift = gift,
                        language = language,
                        onDelete = {
                            scope.launch(Dispatchers.IO) {
                                giftDao.deleteGift(gift)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }

    if (showDialog) {
        AddGiftDialog(
            language = language,
            onDismiss = { showDialog = false },
            onSave = { giftName, fromPerson, note ->
                scope.launch(Dispatchers.IO) {
                    giftDao.insertGift(
                        GiftEntity(
                            userId = userId,
                            giftName = giftName,
                            fromPerson = fromPerson,
                            note = note
                        )
                    )
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun GiftCard(
    gift: GiftEntity,
    language: AppLanguage,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(Color(0xFFFFE1E8), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.CardGiftcard,
                    contentDescription = null,
                    tint = Color(0xFFE95D7E)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = gift.giftName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F3D40)
                )

                if (gift.fromPerson.isNotBlank()) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH)
                            "From: ${gift.fromPerson}"
                        else
                            "Од: ${gift.fromPerson}",
                        fontSize = 12.sp,
                        color = Color(0xFF8F4F5F)
                    )
                }

                if (gift.note.isNotBlank()) {
                    Text(
                        text = gift.note,
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
fun AddGiftDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var giftName by remember { mutableStateOf("") }
    var fromPerson by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (language == AppLanguage.ENGLISH) "Add gift" else "Додај подарок")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = giftName,
                    onValueChange = { giftName = it },
                    label = {
                        Text(if (language == AppLanguage.ENGLISH) "Gift name" else "Име на подарок")
                    },
                    leadingIcon = {
                        Icon(Icons.Filled.CardGiftcard, contentDescription = null)
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = fromPerson,
                    onValueChange = { fromPerson = it },
                    label = {
                        Text(if (language == AppLanguage.ENGLISH) "From person" else "Од кого")
                    },
                    leadingIcon = {
                        Icon(Icons.Filled.Person, contentDescription = null)
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = {
                        Text(if (language == AppLanguage.ENGLISH) "Note" else "Белешка")
                    },
                    leadingIcon = {
                        Icon(Icons.Filled.StickyNote2, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (giftName.isNotBlank()) {
                        onSave(giftName, fromPerson, note)
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