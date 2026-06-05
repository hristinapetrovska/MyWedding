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
import com.example.mywedding.data.TaskEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.first
import com.google.firebase.analytics.FirebaseAnalytics

enum class TaskStatus {
    TODO, IN_PROGRESS, DONE
}

data class TaskCategory(
    val en: String,
    val mk: String
)

val taskCategories = listOf(
    TaskCategory("Venue & Restaurant", "Сала и ресторан"),
    TaskCategory("Food & Drinks", "Храна и пијалоци"),
    TaskCategory("Decoration", "Декорација"),
    TaskCategory("Photography & Video", "Фотографија и видео"),
    TaskCategory("Music", "Музика"),
    TaskCategory("Bride", "Невеста"),
    TaskCategory("Groom", "Младоженец"),
    TaskCategory("Guests", "Гости"),
    TaskCategory("Invitations", "Покани"),
    TaskCategory("Ceremony", "Церемонија"),
    TaskCategory("Documents", "Документи"),
    TaskCategory("Transport", "Превоз"),
    TaskCategory("Cake", "Торта"),
    TaskCategory("Flowers", "Цвеќиња"),
    TaskCategory("Beauty", "Убавина"),
    TaskCategory("Gifts", "Подароци"),
    TaskCategory("Honeymoon", "Меден месец"),
    TaskCategory("Final Week", "Последна недела")
)

fun defaultWeddingTasks(userId: String): List<TaskEntity> {
    fun task(en: String, mk: String, category: TaskCategory) = TaskEntity(
        userId = userId,
        titleEn = en,
        titleMk = mk,
        categoryEn = category.en,
        categoryMk = category.mk,
        status = TaskStatus.TODO.name
    )

    val venue = taskCategories[0]
    val food = taskCategories[1]
    val decor = taskCategories[2]
    val photo = taskCategories[3]
    val music = taskCategories[4]
    val bride = taskCategories[5]
    val groom = taskCategories[6]
    val guests = taskCategories[7]
    val invitations = taskCategories[8]
    val ceremony = taskCategories[9]
    val documents = taskCategories[10]
    val transport = taskCategories[11]
    val cake = taskCategories[12]
    val flowers = taskCategories[13]
    val beauty = taskCategories[14]
    val gifts = taskCategories[15]
    val honeymoon = taskCategories[16]
    val finalWeek = taskCategories[17]

    return listOf(
        task("Choose wedding date", "Избери датум за свадба", ceremony),
        task("Book wedding restaurant", "Резервирај ресторан за свадба", venue),
        task("Visit at least 3 restaurants", "Посети најмалку 3 ресторани", venue),
        task("Check restaurant capacity", "Провери капацитет на ресторан", venue),
        task("Confirm restaurant price per guest", "Потврди цена по гостин", venue),
        task("Choose menu package", "Избери мени пакет", food),
        task("Schedule menu tasting", "Закажи дегустација на мени", food),
        task("Choose drinks package", "Избери пакет пијалоци", food),
        task("Confirm vegetarian options", "Потврди вегетаријански опции", food),
        task("Choose wedding cake", "Избери свадбена торта", cake),
        task("Choose cake flavor", "Избери вкус на тортата", cake),
        task("Choose cake design", "Избери дизајн на тортата", cake),

        task("Book photographer", "Резервирај фотограф", photo),
        task("Book videographer", "Резервирај видеограф", photo),
        task("Prepare photo locations list", "Подготви листа на локации за сликање", photo),
        task("Prepare family photo list", "Подготви листа за семејни фотографии", photo),
        task("Book music band or DJ", "Резервирај бенд или DJ", music),
        task("Choose first dance song", "Избери песна за прв танц", music),
        task("Prepare must-play songs", "Подготви листа на омилени песни", music),
        task("Prepare do-not-play songs", "Подготви листа на песни што не се сакаат", music),

        task("Make guest list", "Направи листа на гости", guests),
        task("Confirm guest attendance", "Потврди присуство на гости", guests),
        task("Create seating plan", "Направи распоред на маси", guests),
        task("Prepare kids table if needed", "Подготви детска маса ако треба", guests),
        task("Prepare guest favors", "Подготви подароци за гости", gifts),

        task("Choose invitation design", "Избери дизајн за покани", invitations),
        task("Order invitations", "Нарачај покани", invitations),
        task("Send invitations", "Испрати покани", invitations),
        task("Prepare digital invitation", "Подготви дигитална покана", invitations),

        task("Choose wedding dress", "Избери венчаница", bride),
        task("Schedule dress fitting", "Закажи проба на венчаница", bride),
        task("Choose bridal shoes", "Избери чевли за невеста", bride),
        task("Choose bridal accessories", "Избери невестински додатоци", bride),
        task("Book hair stylist", "Резервирај фризер", beauty),
        task("Book makeup artist", "Резервирај шминкер", beauty),
        task("Schedule trial makeup", "Закажи пробна шминка", beauty),
        task("Schedule trial hairstyle", "Закажи пробна фризура", beauty),

        task("Choose groom suit", "Избери костум за младоженец", groom),
        task("Choose groom shoes", "Избери чевли за младоженец", groom),
        task("Choose tie or bow tie", "Избери вратоврска или лептир машна", groom),
        task("Schedule suit fitting", "Закажи проба на костум", groom),

        task("Choose rings", "Избери бурми", ceremony),
        task("Prepare ceremony details", "Подготви детали за церемонија", ceremony),
        task("Choose witnesses", "Избери кумови/сведоци", ceremony),
        task("Prepare wedding vows", "Подготви свадбени завети", ceremony),
        task("Prepare required documents", "Подготви потребни документи", documents),
        task("Check municipality appointment", "Провери термин во матично", documents),

        task("Choose decoration theme", "Избери тема за декорација", decor),
        task("Book decorator", "Резервирај декоратер", decor),
        task("Choose table decorations", "Избери декорација за маси", decor),
        task("Choose ceremony decoration", "Избери декорација за церемонија", decor),
        task("Choose flowers", "Избери цвеќиња", flowers),
        task("Choose bridal bouquet", "Избери бидермаер", flowers),
        task("Choose groom boutonniere", "Избери цвет за младоженец", flowers),

        task("Arrange transport", "Организирај превоз", transport),
        task("Book wedding car", "Резервирај свадбено возило", transport),
        task("Plan transport for guests", "Планирај превоз за гости", transport),

        task("Prepare gift registry", "Подготви листа на подароци", gifts),
        task("Plan honeymoon destination", "Испланирај дестинација за меден месец", honeymoon),
        task("Book honeymoon accommodation", "Резервирај сместување за меден месец", honeymoon),

        task("Prepare wedding timeline", "Подготви свадбен распоред", finalWeek),
        task("Confirm all vendors", "Потврди ги сите услуги", finalWeek),
        task("Confirm final guest count", "Потврди конечен број на гости", finalWeek),
        task("Prepare emergency kit", "Подготви мала emergency торбичка", finalWeek),
        task("Pack wedding day bag", "Спакувај торба за свадбениот ден", finalWeek),
        task("Relax one day before wedding", "Одмори се еден ден пред свадбата", finalWeek)
    )
}

fun defaultWeddingTasks(): List<TaskEntity> {
    return defaultWeddingTasks("preview")
}

@Composable
fun TodoScreen(
    language: AppLanguage,
    selectedCategory: String? = null,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val taskDao = remember { DatabaseProvider.getDatabase(context).taskDao() }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"

    val tasks by taskDao.getAllTasks(userId).collectAsState(initial = emptyList())

    var selectedFilter by remember { mutableStateOf("ALL") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<TaskEntity?>(null) }

    LaunchedEffect(userId) {
        withContext(Dispatchers.IO) {
            val existingTasks = taskDao.getAllTasks(userId).first()

            if (existingTasks.isEmpty()) {
                defaultWeddingTasks(userId).forEach {
                    taskDao.insertTask(it)
                }
            }
        }
    }

    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.status == TaskStatus.DONE.name }
    val progress = if (totalTasks == 0) 0f else completedTasks.toFloat() / totalTasks

    val progressText = if (language == AppLanguage.ENGLISH) {
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

    val categoryTasks =
        if (selectedCategory == null) {
            tasks
        } else {
            tasks.filter {
                it.categoryEn == selectedCategory
            }
        }

    val filteredTasks = when (selectedFilter) {
        "TODO" -> categoryTasks.filter { it.status == TaskStatus.TODO.name }
        "IN_PROGRESS" -> categoryTasks.filter { it.status == TaskStatus.IN_PROGRESS.name }
        "DONE" -> categoryTasks.filter { it.status == TaskStatus.DONE.name }
        else -> categoryTasks
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
                    text =
                        if (selectedCategory != null)
                            selectedCategory
                        else if (language == AppLanguage.ENGLISH)
                            "All To-dos"
                        else
                            "Сите задачи",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F3D40)
                )

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(26.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
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
                        text = if (language == AppLanguage.ENGLISH)
                            "$completedTasks of $totalTasks tasks completed"
                        else
                            "$completedTasks од $totalTasks задачи завршени",
                        fontSize = 14.sp,
                        color = Color(0xFF7C6F73)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(filteredTasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        language = language,
                        onClick = {
                            selectedTask = task
                            showDialog = true
                        },
                        onStatusClick = {
                            val nextStatus = when (task.status) {
                                TaskStatus.TODO.name -> TaskStatus.IN_PROGRESS.name
                                TaskStatus.IN_PROGRESS.name -> TaskStatus.DONE.name
                                else -> TaskStatus.TODO.name
                            }

                            scope.launch(Dispatchers.IO) {
                                taskDao.updateTask(task.copy(status = nextStatus))
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        FloatingActionButton(
            onClick = {
                selectedTask = null
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
            TaskDialog(
                language = language,
                task = selectedTask,
                userId = userId,
                onDismiss = { showDialog = false },
                onSave = { task ->
                    scope.launch(Dispatchers.IO) {
                        FirebaseAnalytics
                            .getInstance(context)
                            .logEvent("task_added", null)
                        if (task.id == 0) {
                            taskDao.insertTask(task)
                        } else {
                            taskDao.updateTask(task)
                        }
                    }
                    showDialog = false
                },
                onDelete = { task ->
                    scope.launch(Dispatchers.IO) {
                        taskDao.deleteTask(task)
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
fun TaskCard(
    task: TaskEntity,
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
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = if (task.status == TaskStatus.DONE.name) Color(0xFF6FCF97) else Color(0xFFE4DADD),
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
                    text = if (language == AppLanguage.ENGLISH) task.categoryEn else task.categoryMk,
                    fontSize = 11.sp,
                    color = Color(0xFF8F4F5F)
                )

                if (task.reminder.isNotBlank()) {
                    Text(
                        text = if (language == AppLanguage.ENGLISH)
                            "Reminder: ${task.reminder}"
                        else
                            "Потсетник: ${task.reminder}",
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
    status: String,
    language: AppLanguage
) {
    val text = when (status) {
        TaskStatus.IN_PROGRESS.name -> if (language == AppLanguage.ENGLISH) "In Progress" else "Во тек"
        TaskStatus.DONE.name -> if (language == AppLanguage.ENGLISH) "Done" else "Готово"
        else -> if (language == AppLanguage.ENGLISH) "To do" else "За правење"
    }

    val bgColor = when (status) {
        TaskStatus.IN_PROGRESS.name -> Color(0xFFF4E4FF)
        TaskStatus.DONE.name -> Color(0xFFE2F8E8)
        else -> Color(0xFFFFE1E8)
    }

    val textColor = when (status) {
        TaskStatus.IN_PROGRESS.name -> Color(0xFF9B51E0)
        TaskStatus.DONE.name -> Color(0xFF4CAF50)
        else -> Color(0xFFE95D7E)
    }

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(text = text, fontSize = 10.sp, color = textColor, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun TaskDialog(
    language: AppLanguage,
    task: TaskEntity?,
    userId: String,
    onDismiss: () -> Unit,
    onSave: (TaskEntity) -> Unit,
    onDelete: (TaskEntity) -> Unit
) {
    val isEditing = task != null

    var title by remember { mutableStateOf(if (language == AppLanguage.ENGLISH) task?.titleEn ?: "" else task?.titleMk ?: "") }
    var selectedCategory by remember {
        mutableStateOf(
            taskCategories.find { it.en == task?.categoryEn } ?: taskCategories.first()
        )
    }
    var categoryExpanded by remember { mutableStateOf(false) }
    var reminder by remember { mutableStateOf(task?.reminder ?: "") }
    var note by remember { mutableStateOf(task?.note ?: "") }
    var status by remember { mutableStateOf(task?.status ?: TaskStatus.TODO.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
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
                        text = if (language == AppLanguage.ENGLISH) task.categoryEn else task.categoryMk,
                        fontSize = 12.sp,
                        color = Color(0xFF8F4F5F)
                    )
                } else {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(if (language == AppLanguage.ENGLISH) "Task name" else "Име на задача") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box {
                        OutlinedTextField(
                            value = if (language == AppLanguage.ENGLISH) selectedCategory.en else selectedCategory.mk,
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
                                .clickable { categoryExpanded = true }
                        )

                        DropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            taskCategories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(if (language == AppLanguage.ENGLISH) category.en else category.mk) },
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
                    leadingIcon = { Icon(Icons.Filled.NotificationsNone, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text(if (language == AppLanguage.ENGLISH) "Note" else "Белешка") },
                    leadingIcon = { Icon(Icons.Filled.EditNote, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row {
                    StatusSmallButton(if (language == AppLanguage.ENGLISH) "To do" else "За правење", status == TaskStatus.TODO.name) {
                        status = TaskStatus.TODO.name
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    StatusSmallButton(if (language == AppLanguage.ENGLISH) "In progress" else "Во тек", status == TaskStatus.IN_PROGRESS.name) {
                        status = TaskStatus.IN_PROGRESS.name
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    StatusSmallButton(if (language == AppLanguage.ENGLISH) "Done" else "Готово", status == TaskStatus.DONE.name) {
                        status = TaskStatus.DONE.name
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isEditing) {
                        onSave(
                            task!!.copy(
                                reminder = reminder,
                                note = note,
                                status = status
                            )
                        )
                    } else if (title.isNotBlank()) {
                        onSave(
                            TaskEntity(
                                userId = userId,
                                titleEn = title,
                                titleMk = title,
                                categoryEn = selectedCategory.en,
                                categoryMk = selectedCategory.mk,
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
            Row {
                if (isEditing) {
                    TextButton(onClick = { onDelete(task!!) }) {
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
            .padding(horizontal = 9.dp, vertical = 7.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            color = if (selected) Color.White else Color(0xFF5F4B51)
        )
    }
}