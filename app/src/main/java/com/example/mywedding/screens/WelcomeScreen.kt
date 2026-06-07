package com.example.mywedding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mywedding.AppLanguage
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.mywedding.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun WelcomeScreen(
    language: AppLanguage,
    onBackClick: () -> Unit,
    onGuestLogin: () -> Unit,
    onRegisterClick: () -> Unit,
    onGoogleLogin: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loginMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF7F3))
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 18.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
                tint = Color(0xFF2F3D40)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (language == AppLanguage.ENGLISH) "Welcome to" else "Добредојдовте во",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2F3D40)
            )

            Text(
                text = "MyWedding",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFE95D7E),
                fontFamily = FontFamily.Serif
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (language == AppLanguage.ENGLISH) "Login to continue" else "Најавете се за да продолжите",
                fontSize = 15.sp,
                color = Color(0xFF5F4B51)
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                leadingIcon = {
                    Icon(Icons.Filled.Email, contentDescription = null, tint = Color(0xFF9A9A9A))
                },
                placeholder = {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Email" else "Е-пошта",
                        color = Color(0xFF9A9A9A)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = Color(0xFF9A9A9A))
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = Color(0xFF9A9A9A)
                        )
                    }
                },
                visualTransformation =
                    if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                placeholder = {
                    Text(
                        text = if (language == AppLanguage.ENGLISH) "Password" else "Лозинка",
                        color = Color(0xFF9A9A9A)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(
                onClick = { },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Forgot password?" else "Ја заборавивте лозинката?",
                    color = Color(0xFFE95D7E),
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onGuestLogin()
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
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE95D7E)
                )
            ) {
                Text(
                    text = if (language == AppLanguage.ENGLISH) "Login" else "Најава",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color(0xFFE4DADD))

                Text(
                    text = if (language == AppLanguage.ENGLISH) "  Or continue with  " else "  Или продолжи со  ",
                    fontSize = 13.sp,
                    color = Color(0xFF7C6F73)
                )

                Divider(modifier = Modifier.weight(1f), color = Color(0xFFE4DADD))
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                SocialButton(
                    text = "Google",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onGoogleLogin()
                    }
                )

                Spacer(modifier = Modifier.width(12.dp))

                SocialButton(
                    text = "Facebook",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        loginMessage =
                            if (language == AppLanguage.ENGLISH)
                                "Facebook login will be added next"
                            else
                                "Facebook најава ќе ја додадеме следно"
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    auth.signInAnonymously()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onGuestLogin()
                            } else {
                                loginMessage =
                                    if (language == AppLanguage.ENGLISH)
                                        "Guest login failed"
                                    else
                                        "Најава како гостин не успеа"
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF2F3D40)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color(0xFF2F3D40)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = if (language == AppLanguage.ENGLISH) "Continue as Guest" else "Продолжи како гостин",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (language == AppLanguage.ENGLISH)
                        "Don’t have an account?"
                    else
                        "Немате профил?",
                    fontSize = 14.sp,
                    color = Color(0xFF5F4B51)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = if (language == AppLanguage.ENGLISH) "Sign up" else "Регистрирај се",
                    fontSize = 14.sp,
                    color = Color(0xFFE95D7E),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onRegisterClick() }
                )
            }

            if (loginMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = loginMessage,
                    color = Color(0xFFB00020),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun SocialButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(52.dp)
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFFE4DADD), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(
                id = if (text == "Google")
                    R.drawable.googlelogo
                else
                    R.drawable.fblogo
            ),
            contentDescription = null,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFF2F3D40),
            fontWeight = FontWeight.SemiBold
        )
    }
}