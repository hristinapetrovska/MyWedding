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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import android.Manifest
import android.os.Build
import androidx.compose.runtime.saveable.rememberSaveable

enum class AppScreen {
    WELCOME,
    REGISTER,
    WEDDING_SETUP,
    DASHBOARD
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MyWeddingApp() }
    }
}

@Preview(showBackground = true)
@Composable
fun MyWeddingApp() {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val analytics = FirebaseAnalytics.getInstance(context)

    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            android.util.Log.d("FCM_TOKEN", "Token: ${task.result}")
        }
    }

    var showSplash by rememberSaveable { mutableStateOf(true) }
    var selectedLanguage by rememberSaveable { mutableStateOf<AppLanguage?>(null) }
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.WELCOME) }

    var brideName by rememberSaveable { mutableStateOf("") }
    var groomName by rememberSaveable { mutableStateOf("") }
    var weddingDate by rememberSaveable { mutableStateOf("") }

    fun checkWeddingData() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    brideName = document.getString("brideName") ?: ""
                    groomName = document.getString("groomName") ?: ""
                    weddingDate = document.getString("weddingDate") ?: ""

                    currentScreen =
                        if (
                            brideName.isNotBlank() &&
                            groomName.isNotBlank() &&
                            weddingDate.isNotBlank()
                        ) {
                            AppScreen.DASHBOARD
                        } else {
                            AppScreen.WEDDING_SETUP
                        }
                } else {
                    currentScreen = AppScreen.WEDDING_SETUP
                }
            }
            .addOnFailureListener {
                currentScreen = AppScreen.WEDDING_SETUP
            }
    }

    fun saveWeddingData(bride: String, groom: String, date: String) {
        val uid = auth.currentUser?.uid ?: return

        val data = hashMapOf(
            "brideName" to bride,
            "groomName" to groom,
            "weddingDate" to date
        )

        firestore.collection("users")
            .document(uid)
            .set(data)
            .addOnSuccessListener {
                brideName = bride
                groomName = groom
                weddingDate = date
                currentScreen = AppScreen.DASHBOARD

                analytics.logEvent("wedding_data_saved", null)
            }
    }

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
                            analytics.logEvent("google_login_success", null)
                            checkWeddingData()
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

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

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
                        analytics.logEvent("language_english_selected", null)
                    },
                    onMacedonianClick = {
                        selectedLanguage = AppLanguage.MACEDONIAN
                        analytics.logEvent("language_macedonian_selected", null)
                    }
                )
            } else {
                when (currentScreen) {
                    AppScreen.WELCOME -> {
                        WelcomeScreen(
                            language = selectedLanguage!!,
                            onBackClick = { selectedLanguage = null },
                            onGuestLogin = {
                                analytics.logEvent("guest_login_clicked", null)
                                checkWeddingData()
                            },
                            onRegisterClick = {
                                analytics.logEvent("register_screen_opened", null)
                                currentScreen = AppScreen.REGISTER
                            },
                            onGoogleLogin = {
                                analytics.logEvent("google_login_clicked", null)
                                startGoogleLogin()
                            }
                        )
                    }

                    AppScreen.REGISTER -> {
                        RegisterScreen(
                            language = selectedLanguage!!,
                            onBackClick = { currentScreen = AppScreen.WELCOME },
                            onRegisterSuccess = {
                                analytics.logEvent("register_success", null)
                                checkWeddingData()
                            }
                        )
                    }

                    AppScreen.WEDDING_SETUP -> {
                        WeddingSetupScreen(
                            language = selectedLanguage!!,
                            onBackClick = { currentScreen = AppScreen.WELCOME },
                            onContinueClick = { bride, groom, date ->
                                saveWeddingData(bride, groom, date)
                            }
                        )
                    }

                    AppScreen.DASHBOARD -> {
                        LaunchedEffect(Unit) {
                            analytics.logEvent("dashboard_opened", null)
                        }

                        DashboardScreen(
                            language = selectedLanguage!!,
                            brideName = brideName,
                            groomName = groomName,
                            weddingDate = weddingDate,
                            onLogout = {
                                analytics.logEvent("logout_clicked", null)

                                auth.signOut()

                                brideName = ""
                                groomName = ""
                                weddingDate = ""

                                currentScreen = AppScreen.WELCOME
                            },
                            onSaveWeddingData = { bride, groom, date ->
                                saveWeddingData(bride, groom, date)
                            },
                            onLanguageChange = { newLanguage ->
                                selectedLanguage = newLanguage

                                analytics.logEvent("language_changed_settings", null)
                            }
                        )
                    }
                }
            }
        }
    }
}