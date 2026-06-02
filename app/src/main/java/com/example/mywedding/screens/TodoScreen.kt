package com.example.mywedding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mywedding.AppLanguage

enum class TaskStatus {
    TODO, IN_PROGRESS, DONE
}

data class TaskCategory(
    val en: String,
    val mk: String
)

data class WeddingTask(
    val titleEn: String,
    val titleMk: String,
    val category: TaskCategory,
    val note: String = "",
    val reminder: String = "",
    val status: TaskStatus = TaskStatus.TODO
)

val taskCategories = listOf(
    TaskCategory("Venue", "Сала"),
    TaskCategory("Food & Drinks", "Храна и пијалоци"),
    TaskCategory("Decoration", "Декорација"),
    TaskCategory("Photography", "Фотографија"),
    TaskCategory("Music", "Музика"),
    TaskCategory("Bride", "Невеста"),
    TaskCategory("Groom", "Младоженец"),
    TaskCategory("Guests", "Гости"),
    TaskCategory("Ceremony", "Церемонија"),
    TaskCategory("Documents", "Документи"),
    TaskCategory("Transport", "Превоз"),
    TaskCategory("Cake", "Торта"),
    TaskCategory("Flowers", "Цвеќиња"),
    TaskCategory("Beauty", "Убавина"),
    TaskCategory("Gifts", "Подароци")
)

fun defaultWeddingTasks(): List<WeddingTask> {
    val venue = taskCategories[0]
    val food = taskCategories[1]
    val decor = taskCategories[2]
    val photo = taskCategories[3]
    val music = taskCategories[4]
    val bride = taskCategories[5]
    val groom = taskCategories[6]
    val guests = taskCategories[7]
    val ceremony = taskCategories[8]
    val documents = taskCategories[9]
    val transport = taskCategories[10]
    val cake = taskCategories[11]
    val flowers = taskCategories[12]
    val beauty = taskCategories[13]
    val gifts = taskCategories[14]

    return listOf(
        WeddingTask("Book wedding venue", "Резервирај сала за свадба", venue),
        WeddingTask("Choose restaurant menu", "Избери мени во ресторан", food),
        WeddingTask("Schedule menu tasting", "Закажи дегустација на мени", food),
        WeddingTask("Choose wedding cake", "Избери свадбена торта", cake),
        WeddingTask("Choose cake design", "Избери дизајн на тортата", cake),
        WeddingTask("Book photographer", "Резервирај фотограф", photo),
        WeddingTask("Book videographer", "Резервирај видеограф", photo),
        WeddingTask("Book music band / DJ", "Резервирај музика / DJ", music),
        WeddingTask("Choose first dance song", "Избери песна за прв танц", music),
        WeddingTask("Make guest list", "Направи листа на гости", guests),
        WeddingTask("Send invitations", "Испрати покани", guests),
        WeddingTask("Confirm guest attendance", "Потврди присуство на гости", guests),
        WeddingTask("Create seating plan", "Направи распоред на маси", guests),
        WeddingTask("Choose wedding dress", "Избери венчаница", bride),
        WeddingTask("Schedule dress fitting", "Закажи проба на венчаница", bride),
        WeddingTask("Choose bridal shoes", "Избери чевли за невеста", bride),
        WeddingTask("Book hair stylist", "Резервирај фризер", beauty),
        WeddingTask("Book makeup artist", "Резервирај шминкер", beauty),
        WeddingTask("Choose groom suit", "Избери костум за младоженец", groom),
        WeddingTask("Choose groom shoes", "Избери чевли за младоженец", groom),
        WeddingTask("Choose rings", "Избери бурми", ceremony),
        WeddingTask("Prepare ceremony details", "Подготви детали за церемонија", ceremony),
        WeddingTask("Prepare documents", "Подготви документи", documents),
        WeddingTask("Choose decoration theme", "Избери тема за декорација", decor),
        WeddingTask("Book decorator", "Резервирај декоратер", decor),
        WeddingTask("Choose flowers", "Избери цвеќиња", flowers),
        WeddingTask("Choose bridal bouquet", "Избери бидермаер", flowers),
        WeddingTask("Arrange transport", "Организирај превоз", transport),
        WeddingTask("Prepare gift registry", "Подготви листа на подароци", gifts),
        WeddingTask("Prepare wedding timeline", "Подготви свадбен распоред", ceremony)
    )
}

@Composable
fun TodoScreen(
    language: AppLanguage,
    onBackClick: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("ALL") }
    var showDialog by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }

    var tasks by remember {
        mutableStateOf(defaultWeddingTasks())
    }

    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.status == TaskStatus.DONE }
    val progress = if (totalTasks == 0) 0f else completedTasks.toFloat() / totalTasks

    val progressText =
        if (language == AppLanguage.ENGLISH) {
            when {
                progress == 0f -> "Let's get started!"
                progress < 0.5f -> "Keep going!"
                progress < 1f -> "You're doing great!"
                else -> "Everything is completed!"
            }
        } else {
            when {
                progress == 0f -> "Ајде да започнеме!"
                progress < 0.5f -> "Продолжете така!"
                progress < 1f -> "Одлично ви оди!"
                else -> "Сè е завршено!"
            }
        }

    val filteredTasks = when (selectedFilter) {
        "TODO" -> tasks.filter { it.status == TaskStatus.TODO }
        "IN_PROGRESS" -> tasks.filter { it.status == TaskStatus.IN_PROGRESS }
        "DONE" -> tasks.filter { it.status == TaskStatus.DONE }
        else -> tasks
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7F3))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp)
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
                    text = if (language == AppLanguage.ENGLISH) "All To-dos" else "Сите задачи",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F3D40)
                )

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(26.dp))
            }

            Spacer(modifier = Modifier.height(26.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProgressCircle(progress = progress)

                Spacer(modifier = Modifier.width(18.dp))

                Column {
                    Text(
                        text = progressText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2F3D40)
                    )

                    Text(
                        text =
                            if (language == AppLanguage.ENGLISH)
                                "$completedTasks of $totalTasks tasks completed"
                            else
                                "$completedTasks од $totalTasks задачи завршени",
                        fontSize = 14.sp,
                        color = Color(0xFF7C6F73)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                FilterChipButton("ALL", if (language == AppLanguage.ENGLISH) "All" else "Сите", selectedFilter) {
                    selectedFilter = "ALL"
                }

                Spacer(modifier = Modifier.width(8.dp))

                FilterChipButton("TODO", "To do", selectedFilter) {
                    selectedFilter = "TODO"
                }

                Spacer(modifier = Modifier.width(8.dp))

                FilterChipButton("IN_PROGRESS", if (language == AppLanguage.ENGLISH) "In Progress" else "Во тек", selectedFilter) {
                    selectedFilter = "IN_PROGRESS"
                }

                Spacer(modifier = Modifier.width(8.dp))

                FilterChipButton("DONE", if (language == AppLanguage.ENGLISH) "Done" else "Готово", selectedFilter) {
                    selectedFilter = "DONE"
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            filteredTasks.forEach { task ->
                val realIndex = tasks.indexOf(task)

                TaskCard(
                    task = task,
                    language = language,
                    onClick = {
                        editingIndex = realIndex
                        showDialog = true
                    },
                    onStatusClick = {
                        tasks = tasks.mapIndexed { index, item ->
                            if (index == realIndex) {
                                val nextStatus = when (item.status) {
                                    TaskStatus.TODO -> TaskStatus.IN_PROGRESS
                                    TaskStatus.IN_PROGRESS -> TaskStatus.DONE
                                    TaskStatus.DONE -> TaskStatus.TODO
                                }
                                item.copy(status = nextStatus)
                            } else item
                        }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        FloatingActionButton(
            onClick = {
                editingIndex = null
                showDialog = true
            },
            containerColor = Color(0xFFE95D7E),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null
            )
        }

        if (showDialog) {
            TaskDialog(
                language = language,
                task = editingIndex?.let { tasks[it] },
                onDismiss = { showDialog = false },
                onSave = { newTask ->
                    tasks =
                        if (editingIndex == null) {
                            tasks + newTask
                        } else {
                            tasks.mapIndexed { index, item ->
                                if (index == editingIndex) newTask else item
                            }
                        }

                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun FilterChipButton(
    key: String,
    text: String,
    selectedFilter: String,
    onClick: () -> Unit
) {
    val selected = key == selectedFilter

    Box(
        modifier = Modifier
            .background(
                color = if (selected) Color(0xFFE95D7E) else Color.White,
                shape = RoundedCornerShape(50)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 9.dp)
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
fun TaskCard(
    task: WeddingTask,
    language: AppLanguage,
    onClick: () -> Unit,
    onStatusClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = if (task.status == TaskStatus.DONE) Color(0xFF6FCF97) else Color(0xFFE4DADD),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onStatusClick() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) task.titleEn else task.titleMk,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2F3D40)
                )

                Text(
                    text = if (language == AppLanguage.ENGLISH) task.category.en else task.category.mk,
                    fontSize = 11.sp,
                    color = Color(0xFF8F4F5F)
                )

                if (task.reminder.isNotBlank() || task.note.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text =
                            if (task.reminder.isNotBlank())
                                if (language == AppLanguage.ENGLISH) "Reminder: ${task.reminder}" else "Потсетник: ${task.reminder}"
                            else
                                task.note,
                        fontSize = 11.sp,
                        color = Color(0xFF8F4F5F)
                    )
                }
            }

            StatusBadge(task.status, language)
        }
    }
}

@Composable
fun StatusBadge(
    status: TaskStatus,
    language: AppLanguage
) {
    val text = when (status) {
        TaskStatus.TODO -> if (language == AppLanguage.ENGLISH) "To do" else "За правење"
        TaskStatus.IN_PROGRESS -> if (language == AppLanguage.ENGLISH) "In Progress" else "Во тек"
        TaskStatus.DONE -> if (language == AppLanguage.ENGLISH) "Done" else "Готово"
    }

    val bgColor = when (status) {
        TaskStatus.TODO -> Color(0xFFFFE1E8)
        TaskStatus.IN_PROGRESS -> Color(0xFFF4E4FF)
        TaskStatus.DONE -> Color(0xFFE2F8E8)
    }

    val textColor = when (status) {
        TaskStatus.TODO -> Color(0xFFE95D7E)
        TaskStatus.IN_PROGRESS -> Color(0xFF9B51E0)
        TaskStatus.DONE -> Color(0xFF4CAF50)
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
fun TaskDialog(
    language: AppLanguage,
    task: WeddingTask?,
    onDismiss: () -> Unit,
    onSave: (WeddingTask) -> Unit
) {
    var titleEn by remember { mutableStateOf(task?.titleEn ?: "") }
    var titleMk by remember { mutableStateOf(task?.titleMk ?: "") }
    var selectedCategory by remember { mutableStateOf(task?.category ?: taskCategories.first()) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var reminder by remember { mutableStateOf(task?.reminder ?: "") }
    var note by remember { mutableStateOf(task?.note ?: "") }
    var status by remember { mutableStateOf(task?.status ?: TaskStatus.TODO) }

    val isEditing = task != null

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if ((language == AppLanguage.ENGLISH && titleEn.isNotBlank()) ||
                        (language == AppLanguage.MACEDONIAN && titleMk.isNotBlank()) ||
                        isEditing
                    ) {
                        onSave(
                            WeddingTask(
                                titleEn = if (isEditing) task!!.titleEn else titleEn,
                                titleMk = if (isEditing) task!!.titleMk else titleMk,
                                category = if (isEditing) task!!.category else selectedCategory,
                                reminder = reminder,
                                note = note,
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
            TextButton(onClick = onDismiss) {
                Text(if (language == AppLanguage.ENGLISH) "Cancel" else "Откажи")
            }
        },
        title = {
            Text(
                text = if (isEditing)
                    if (language == AppLanguage.ENGLISH) "Edit task" else "Измени задача"
                else
                    if (language == AppLanguage.ENGLISH) "Add task" else "Додај задача"
            )
        },
        text = {
            Column {
                if (isEditing) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) task!!.titleEn else task!!.titleMk,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2F3D40)
                    )

                    Text(
                        text = if (language == AppLanguage.ENGLISH) task.category.en else task.category.mk,
                        fontSize = 12.sp,
                        color = Color(0xFF8F4F5F)
                    )
                } else {
                    OutlinedTextField(
                        value = if (language == AppLanguage.ENGLISH) titleEn else titleMk,
                        onValueChange = {
                            if (language == AppLanguage.ENGLISH) titleEn = it else titleMk = it
                        },
                        label = { Text(if (language == AppLanguage.ENGLISH) "Task name" else "Име на задача") },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box {
                        OutlinedTextField(
                            value = if (language == AppLanguage.ENGLISH)
                                selectedCategory.en
                            else
                                selectedCategory.mk,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text(if (language == AppLanguage.ENGLISH) "Category" else "Категорија")
                            },
                            trailingIcon = {
                                Text(
                                    text = "▼",
                                    modifier = Modifier.clickable {
                                        categoryExpanded = true
                                    }
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable {
                                    categoryExpanded = true
                                }
                        )

                        DropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = {
                                categoryExpanded = false
                            }
                        ) {
                            taskCategories.forEach { category ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            if (language == AppLanguage.ENGLISH)
                                                category.en
                                            else
                                                category.mk
                                        )
                                    },
                                    onClick = {
                                        selectedCategory = category
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = reminder,
                    onValueChange = { reminder = it },
                    label = { Text(if (language == AppLanguage.ENGLISH) "Reminder" else "Потсетник") },
                    leadingIcon = {
                        Icon(Icons.Filled.NotificationsNone, contentDescription = null)
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text(if (language == AppLanguage.ENGLISH) "Note" else "Белешка") },
                    leadingIcon = {
                        Icon(Icons.Filled.EditNote, contentDescription = null)
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row {
                    StatusSmallButton(
                        if (language == AppLanguage.ENGLISH) "To do" else "За правење",
                        status == TaskStatus.TODO
                    ) {
                        status = TaskStatus.TODO
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    StatusSmallButton(
                        if (language == AppLanguage.ENGLISH) "In progress" else "Во тек",
                        status == TaskStatus.IN_PROGRESS
                    ) {
                        status = TaskStatus.IN_PROGRESS
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    StatusSmallButton(
                        if (language == AppLanguage.ENGLISH) "Done" else "Готово",
                        status == TaskStatus.DONE
                    ) {
                        status = TaskStatus.DONE
                    }
                }
            }
        }
    )
}

@Composable
fun StatusSmallButton(
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
            .padding(horizontal = 10.dp, vertical = 7.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            color = if (selected) Color.White else Color(0xFF5F4B51)
        )
    }
}