package com.example.mywedding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mywedding.AppLanguage
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterScreen(
    language: AppLanguage,
    onBackClick: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var registerMessage by remember { mutableStateOf("") }

    val titleText =
        if (language == AppLanguage.ENGLISH) "Create account" else "Креирај профил"

    val subtitleText =
        if (language == AppLanguage.ENGLISH)
            "Register to save your wedding plan"
        else
            "Регистрирајте се за да го зачувате вашиот план"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7F3)),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(20.dp)
                .size(60.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                tint = Color(0xFF8F4F5F),
                modifier = Modifier.size(34.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titleText,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7D4B57),
                fontFamily = FontFamily.Serif
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "MyWedding",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFE95D7E),
                fontFamily = FontFamily.Serif
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = subtitleText,
                color = Color(0xFF9A7B83),
                fontSize = 15.sp,
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(36.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = null,
                        tint = Color(0xFFE95D7E)
                    )
                },
                placeholder = {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Email" else "Е-пошта",
                        fontWeight = FontWeight.Bold
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

            Spacer(modifier = Modifier.height(18.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = Color(0xFFE95D7E)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector =
                                if (passwordVisible) Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = Color(0xFF8F4F5F)
                        )
                    }
                },
                visualTransformation =
                    if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                placeholder = {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Password" else "Лозинка",
                        fontWeight = FontWeight.Bold
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

            Spacer(modifier = Modifier.height(18.dp))

            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = Color(0xFFE95D7E)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector =
                                if (confirmPasswordVisible) Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = Color(0xFF8F4F5F)
                        )
                    }
                },
                visualTransformation =
                    if (confirmPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                placeholder = {
                    Text(
                        text =
                            if (language == AppLanguage.ENGLISH)
                                "Confirm password"
                            else
                                "Потврди лозинка",
                        fontWeight = FontWeight.Bold
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

            Spacer(modifier = Modifier.height(22.dp))

            if (registerMessage.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFFFE1E8),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = registerMessage,
                        color = Color(0xFFB00020),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = {
                    when {
                        email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                            registerMessage =
                                if (language == AppLanguage.ENGLISH)
                                    "Please fill all fields"
                                else
                                    "Пополнете ги сите полиња"
                        }

                        password != confirmPassword -> {
                            registerMessage =
                                if (language == AppLanguage.ENGLISH)
                                    "Passwords do not match"
                                else
                                    "Лозинките не се совпаѓаат"
                        }

                        password.length < 6 -> {
                            registerMessage =
                                if (language == AppLanguage.ENGLISH)
                                    "Password must be at least 6 characters"
                                else
                                    "Лозинката мора да има најмалку 6 карактери"
                        }

                        else -> {
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        onRegisterSuccess()
                                    } else {
                                        registerMessage =
                                            task.exception?.message
                                                ?: if (language == AppLanguage.ENGLISH)
                                                    "Registration failed"
                                                else
                                                    "Регистрацијата не е успешна"
                                    }
                                }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE95D7E)
                )
            ) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Create Account" else "Креирај профил",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}