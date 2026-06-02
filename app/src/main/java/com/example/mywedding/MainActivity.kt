package com.example.mywedding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import com.example.mywedding.screens.MyWeddingSplash
import com.example.mywedding.screens.LanguageScreen
import com.example.mywedding.screens.WelcomeScreen
import com.example.mywedding.screens.WeddingSetupScreen
import com.example.mywedding.screens.DashboardScreen


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyWeddingApp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyWeddingApp() {
    var showSplash by remember { mutableStateOf(true) }
    var selectedLanguage by remember { mutableStateOf<AppLanguage?>(null) }
    var showWeddingSetup by remember { mutableStateOf(false) }
    var showDashboard by remember { mutableStateOf(false) }

    var brideName by remember { mutableStateOf("") }
    var groomName by remember { mutableStateOf("") }
    var weddingDate by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2500)
        showSplash = false
    }

    AnimatedContent(
        targetState = showSplash,
        transitionSpec = {
            fadeIn(animationSpec = tween(1200)) togetherWith
                    fadeOut(animationSpec = tween(1200))
        },
        label = ""
    ) { splashVisible ->

        if (splashVisible) {
            MyWeddingSplash()
        } else {
            if (selectedLanguage == null) {
                LanguageScreen(
                    onEnglishClick = {
                        selectedLanguage = AppLanguage.ENGLISH
                    },

                    onMacedonianClick = {
                        selectedLanguage = AppLanguage.MACEDONIAN
                    }
                )
            }  else {
                if (showDashboard) {
                    DashboardScreen(
                        language = selectedLanguage!!,
                        brideName = brideName,
                        groomName = groomName,
                        weddingDate = weddingDate
                    )
                } else if (showWeddingSetup) {
                    WeddingSetupScreen(
                        language = selectedLanguage!!,
                        onBackClick = {
                            showWeddingSetup = false
                        },
                        onContinueClick = { bride, groom, date ->
                            brideName = bride
                            groomName = groom
                            weddingDate = date
                            showDashboard = true
                        }
                    )
                } else {
                    WelcomeScreen(
                        language = selectedLanguage!!,
                        onBackClick = {
                            selectedLanguage = null
                        },
                        onGuestLogin = {
                            showWeddingSetup = true
                        }
                    )
                }
            }
        }
    }
}