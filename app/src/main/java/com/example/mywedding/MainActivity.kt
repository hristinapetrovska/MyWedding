package com.example.mywedding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.mywedding.screens.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

enum class AppScreen {
    WELCOME,
    REGISTER,
    WEDDING_SETUP,
    DASHBOARD
}

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
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var showSplash by remember { mutableStateOf(true) }
    var selectedLanguage by remember { mutableStateOf<AppLanguage?>(null) }
    var currentScreen by remember { mutableStateOf(AppScreen.WELCOME) }

    var brideName by remember { mutableStateOf("") }
    var groomName by remember { mutableStateOf("") }
    var weddingDate by remember { mutableStateOf("") }

    val googleLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                auth.signInWithCredential(credential)
                    .addOnCompleteListener { firebaseTask ->
                        if (firebaseTask.isSuccessful) {
                            currentScreen = AppScreen.WEDDING_SETUP
                        }
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    fun startGoogleLogin() {
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        val googleClient = GoogleSignIn.getClient(context, googleSignInOptions)
        googleLauncher.launch(googleClient.signInIntent)
    }

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
            } else {
                when (currentScreen) {

                    AppScreen.WELCOME -> {
                        WelcomeScreen(
                            language = selectedLanguage!!,
                            onBackClick = {
                                selectedLanguage = null
                            },
                            onGuestLogin = {
                                currentScreen = AppScreen.WEDDING_SETUP
                            },
                            onRegisterClick = {
                                currentScreen = AppScreen.REGISTER
                            },
                            onGoogleLogin = {
                                startGoogleLogin()
                            }
                        )
                    }

                    AppScreen.REGISTER -> {
                        RegisterScreen(
                            language = selectedLanguage!!,
                            onBackClick = {
                                currentScreen = AppScreen.WELCOME
                            },
                            onRegisterSuccess = {
                                currentScreen = AppScreen.WEDDING_SETUP
                            }
                        )
                    }

                    AppScreen.WEDDING_SETUP -> {
                        WeddingSetupScreen(
                            language = selectedLanguage!!,
                            onBackClick = {
                                currentScreen = AppScreen.WELCOME
                            },
                            onContinueClick = { bride, groom, date ->
                                brideName = bride
                                groomName = groom
                                weddingDate = date
                                currentScreen = AppScreen.DASHBOARD
                            }
                        )
                    }

                    AppScreen.DASHBOARD -> {
                        DashboardScreen(
                            language = selectedLanguage!!,
                            brideName = brideName,
                            groomName = groomName,
                            weddingDate = weddingDate
                        )
                    }
                }
            }
        }
    }
}