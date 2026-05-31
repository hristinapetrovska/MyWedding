package com.example.mywedding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun WelcomeScreen(
    language: AppLanguage,
    onBackClick: () -> Unit,
    onGuestLogin: () -> Unit
) {
    val welcomeText =
        if (language == AppLanguage.ENGLISH) {
            "Welcome to"
        } else {
            "Добредојдовте во"
        }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loginMessage by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()

    val subtitleText =
        if (language == AppLanguage.ENGLISH) {
            "Login to continue"
        } else {
            "Најавете се за да продолжите"
        }

    val emailText =
        if (language == AppLanguage.ENGLISH) {
            "Email"
        } else {
            "Е-пошта"
        }

    val passwordText =
        if (language == AppLanguage.ENGLISH) {
            "Password"
        } else {
            "Лозинка"
        }

    val loginText =
        if (language == AppLanguage.ENGLISH) {
            "Login"
        } else {
            "Најава"
        }

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
                contentDescription = "Back",
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

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = welcomeText,
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
                onValueChange = {
                    email = it
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription =
                            if (language == AppLanguage.ENGLISH)
                                "Email"
                            else
                                "Е-пошта",
                        tint = Color(0xFFE95D7E)
                    )
                },
                placeholder = {
                    Text(
                        text = emailText,
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
                ),
            )

            Spacer(modifier = Modifier.height(18.dp))

            TextField(
                value = password,
                onValueChange = {
                    password = it
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription =
                            if (language == AppLanguage.ENGLISH)
                                "Password"
                            else
                                "Лозинка",
                        tint = Color(0xFFE95D7E)
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff,
                            contentDescription =
                                if (language == AppLanguage.ENGLISH)
                                    "Show password"
                                else
                                    "Прикажи лозинка",
                            tint = Color(0xFF8F4F5F)
                        )
                    }
                },
                visualTransformation =
                    if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                placeholder = {
                    Text(
                        text = passwordText,
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

            Spacer(modifier = Modifier.height(14.dp))

            TextButton(
                onClick = { },
                modifier = Modifier.align(Alignment.End)
            )
            {
                Text(
                    fontSize = 13.sp,
                    text =
                        if (language == AppLanguage.ENGLISH)
                            "Forgot Password?"
                        else
                            "Ја заборавивте лозинката?",
                    color = Color(0xFFE95D7E)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                loginMessage =
                                    if (language == AppLanguage.ENGLISH)
                                        "Login successful"
                                    else
                                        "Успешна најава"
                            } else {
                                loginMessage =
                                    if (language == AppLanguage.ENGLISH)
                                        "Login failed"
                                    else
                                        "Неуспешна најава"
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
            )
            {
                Text(
                    text = loginText,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (loginMessage.isNotEmpty()) {
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
                        text = loginMessage,
                        color = Color(0xFFB00020),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = {
                    auth.signInAnonymously()
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                onGuestLogin()

                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF8F4F5F)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription =
                        if (language == AppLanguage.ENGLISH)
                            "Guest"
                        else
                            "Гостин",
                    tint = Color(0xFFE95D7E)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text =
                        if (language == AppLanguage.ENGLISH)
                            "Continue as Guest"
                        else
                            "Продолжи како гостин",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
