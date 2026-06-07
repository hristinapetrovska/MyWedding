package com.example.mywedding.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mywedding.AppLanguage
import com.example.mywedding.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeddingSetupScreen(
    language: AppLanguage,
    onBackClick: () -> Unit,
    onContinueClick: (String, String, String) -> Unit

) {
    var brideName by remember { mutableStateOf("") }
    var groomName by remember { mutableStateOf("") }
    var weddingDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.weddingsetup),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.25f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 34.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = if (language == AppLanguage.ENGLISH) "Back" else "Назад",
                        tint = Color(0xFF8F4F5F)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (index == 0) 15.dp else 10.dp)
                            .background(
                                color = if (index == 0) Color(0xFFE95D7E) else Color(0xFFE4DADD),
                                shape = CircleShape
                            )
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color(0xFFD8CDD1))
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = if (language == AppLanguage.ENGLISH)
                    "Let's set up\nyour wedding"
                else
                    "Ајде да ја организираме\nвашата свадба",
                fontSize = 30.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2F3D40),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (language == AppLanguage.ENGLISH)
                    "Tell us a few details about your big day"
                else
                    "Внесете неколку детали за вашиот голем ден",
                fontSize = 14.sp,
                color = Color(0xFF7C6F73),
                fontFamily = FontFamily.SansSerif
            )

            Spacer(modifier = Modifier.height(30.dp))

            SetupInputField(
                value = brideName,
                onValueChange = { brideName = it },
                placeholder = if (language == AppLanguage.ENGLISH) "Bride name" else "Име на невеста"
            )

            Spacer(modifier = Modifier.height(16.dp))

            SetupInputField(
                value = groomName,
                onValueChange = { groomName = it },
                placeholder = if (language == AppLanguage.ENGLISH) "Groom name" else "Име на младоженец"
            )

            Spacer(modifier = Modifier.height(16.dp))

            DateInputField(
                value = weddingDate,
                label = if (language == AppLanguage.ENGLISH) "Wedding Date" else "Датум на свадба",
                placeholder = if (language == AppLanguage.ENGLISH) "Select wedding date" else "Избери датум",
                onClick = { showDatePicker = true }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                enabled =
                    brideName.isNotBlank() &&
                            groomName.isNotBlank() &&
                            weddingDate.isNotBlank(),

                onClick = {
                    onContinueClick(
                        brideName,
                        groomName,
                        weddingDate
                    )
                },                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 35.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE95D7E))
            ) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Continue" else "Продолжи",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val selectedMillis = datePickerState.selectedDateMillis
                            if (selectedMillis != null) {
                                val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                weddingDate = formatter.format(Date(selectedMillis))
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text(if (language == AppLanguage.ENGLISH) "OK" else "Во ред")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(if (language == AppLanguage.ENGLISH) "Cancel" else "Откажи")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
fun SetupInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color(0xFF9A8B90),
                    fontWeight = FontWeight.Normal
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color(0xFFE95D7E)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun DateInputField(
    value: String,
    label: String,
    placeholder: String,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF4D4448),
            fontFamily = FontFamily.SansSerif
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
        ) {
            TextField(
                value = value,
                onValueChange = {},
                enabled = false,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        tint = Color(0xFFE95D7E)
                    )
                },
                placeholder = {
                    Text(
                        text = placeholder,
                        color = Color(0xFF9A8B90),
                        fontWeight = FontWeight.Normal
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    disabledTextColor = Color(0xFF5F4B51),
                    disabledPlaceholderColor = Color(0xFF9A8B90),
                    disabledLeadingIconColor = Color(0xFFE95D7E),
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }
    }
}