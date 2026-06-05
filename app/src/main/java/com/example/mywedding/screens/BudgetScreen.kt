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
import com.example.mywedding.data.BudgetEntity
import com.example.mywedding.data.DatabaseProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.firebase.analytics.FirebaseAnalytics
data class BudgetCategory(
    val en: String,
    val mk: String
)

val budgetCategories = listOf(
    BudgetCategory("Venue & Restaurant", "Сала и ресторан"),
    BudgetCategory("Food & Drinks", "Храна и пијалоци"),
    BudgetCategory("Decoration", "Декорација"),
    BudgetCategory("Photography & Video", "Фотографија и видео"),
    BudgetCategory("Music", "Музика"),
    BudgetCategory("Bride", "Невеста"),
    BudgetCategory("Groom", "Младоженец"),
    BudgetCategory("Cake", "Торта"),
    BudgetCategory("Flowers", "Цвеќиња"),
    BudgetCategory("Transport", "Превоз"),
    BudgetCategory("Invitations", "Покани"),
    BudgetCategory("Gifts", "Подароци"),
    BudgetCategory("Beauty", "Убавина"),
    BudgetCategory("Documents", "Документи"),
    BudgetCategory("Other", "Друго")
)

@Composable
fun BudgetScreen(
    language: AppLanguage,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val budgetDao = remember { DatabaseProvider.getDatabase(context).budgetDao() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"

    val items by budgetDao.getAllBudgetItems(userId).collectAsState(initial = emptyList())
    val totalSpent = items.sumOf { it.amount }

    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<BudgetEntity?>(null) }

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
                    text = if (language == AppLanguage.ENGLISH) "Budget" else "Буџет",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F3D40)
                )

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(26.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color(0xFFFFE1E8), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AttachMoney,
                            contentDescription = null,
                            tint = Color(0xFFE95D7E),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "Total spent" else "Вкупно потрошено",
                            fontSize = 14.sp,
                            color = Color(0xFF8F4F5F)
                        )

                        Text(
                            text = "${totalSpent.toInt()} ден",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF2F3D40)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            LazyColumn {
                items(items, key = { it.id }) { item ->
                    BudgetCard(
                        item = item,
                        onClick = {
                            selectedItem = item
                            showDialog = true
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        FloatingActionButton(
            onClick = {
                selectedItem = null
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
            BudgetDialog(
                language = language,
                item = selectedItem,
                userId = userId,
                onDismiss = { showDialog = false },
                onSave = { item ->
                    scope.launch(Dispatchers.IO) {
                        FirebaseAnalytics
                            .getInstance(context)
                            .logEvent("budget_item_added", null)
                        if (item.id == 0) {
                            budgetDao.insertBudgetItem(item)
                        } else {
                            budgetDao.updateBudgetItem(item)
                        }
                    }
                    showDialog = false
                },
                onDelete = { item ->
                    scope.launch(Dispatchers.IO) {
                        budgetDao.deleteBudgetItem(item)
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun BudgetCard(
    item: BudgetEntity,
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
                    imageVector = Icons.Filled.ReceiptLong,
                    contentDescription = null,
                    tint = Color(0xFFE95D7E)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F3D40)
                )

                Text(
                    text = item.category,
                    fontSize = 12.sp,
                    color = Color(0xFF8F4F5F)
                )

                if (item.note.isNotBlank()) {
                    Text(
                        text = item.note,
                        fontSize = 12.sp,
                        color = Color(0xFF8F4F5F)
                    )
                }
            }

            Text(
                text = "${item.amount.toInt()} ден",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE95D7E)
            )
        }
    }
}

@Composable
fun BudgetDialog(
    language: AppLanguage,
    item: BudgetEntity?,
    userId: String,
    onDismiss: () -> Unit,
    onSave: (BudgetEntity) -> Unit,
    onDelete: (BudgetEntity) -> Unit
) {
    val isEditing = item != null

    var title by remember { mutableStateOf(item?.title ?: "") }
    var amountText by remember {
        mutableStateOf(
            if ((item?.amount ?: 0.0) == 0.0) "" else item?.amount?.toInt().toString()
        )
    }

    var category by remember {
        mutableStateOf(
            budgetCategories.find {
                it.en == item?.category || it.mk == item?.category
            } ?: budgetCategories.first()
        )
    }

    var note by remember { mutableStateOf(item?.note ?: "") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEditing)
                    if (language == AppLanguage.ENGLISH) "Edit expense" else "Измени трошок"
                else
                    if (language == AppLanguage.ENGLISH) "Add expense" else "Додај трошок"
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(if (language == AppLanguage.ENGLISH) "Expense name" else "Име на трошок") },
                    leadingIcon = { Icon(Icons.Filled.ReceiptLong, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { char -> char.isDigit() } },
                    label = { Text(if (language == AppLanguage.ENGLISH) "Amount" else "Износ") },
                    leadingIcon = { Icon(Icons.Filled.AttachMoney, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box {
                    OutlinedTextField(
                        value = if (language == AppLanguage.ENGLISH) category.en else category.mk,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (language == AppLanguage.ENGLISH) "Category" else "Категорија") },
                        trailingIcon = {
                            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { expanded = true }
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        budgetCategories.forEach { cat ->
                            DropdownMenuItem(
                                text = {
                                    Text(if (language == AppLanguage.ENGLISH) cat.en else cat.mk)
                                },
                                onClick = {
                                    category = cat
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text(if (language == AppLanguage.ENGLISH) "Note" else "Белешка") },
                    leadingIcon = { Icon(Icons.Filled.StickyNote2, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0

                    if (title.isNotBlank() && amount > 0) {
                        onSave(
                            BudgetEntity(
                                id = item?.id ?: 0,
                                userId = userId,
                                title = title,
                                category = if (language == AppLanguage.ENGLISH) category.en else category.mk,
                                amount = amount,
                                note = note
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
                    TextButton(onClick = { onDelete(item!!) }) {
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