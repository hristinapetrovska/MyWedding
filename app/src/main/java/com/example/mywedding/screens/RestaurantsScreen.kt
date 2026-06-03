package com.example.mywedding.screens

import android.Manifest
import android.content.Intent
import android.location.Location
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

@Composable
fun RestaurantsScreen(
    language: AppLanguage,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var userLocation by remember { mutableStateOf<Location?>(null) }
    var message by remember {
        mutableStateOf(
            if (language == AppLanguage.ENGLISH)
                "Allow location to find nearby restaurants."
            else
                "Дозволи локација за да најдеш ресторани во близина."
        )
    }

    fun loadLocation() {
        try {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { location ->
                if (location != null) {
                    userLocation = location
                    message = if (language == AppLanguage.ENGLISH)
                        "Location detected successfully."
                    else
                        "Локацијата е успешно пронајдена."
                } else {
                    message = if (language == AppLanguage.ENGLISH)
                        "Location not found. Turn on GPS and try again."
                    else
                        "Локацијата не е пронајдена. Вклучи GPS и пробај повторно."
                }
            }
        } catch (e: SecurityException) {
            message = if (language == AppLanguage.ENGLISH)
                "Location permission error."
            else
                "Грешка при пристап до локацијата."
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            loadLocation()
        } else {
            message = if (language == AppLanguage.ENGLISH)
                "Location permission denied."
            else
                "Не е дозволен пристап до локацијата."
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
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
                    text = if (language == AppLanguage.ENGLISH) "Restaurants" else "Ресторани",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F3D40)
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = null,
                    tint = Color(0xFFE95D7E),
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFFFFE1E8), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFFE95D7E),
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (language == AppLanguage.ENGLISH)
                            "Nearby wedding restaurants"
                        else
                            "Ресторани во близина",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2F3D40)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = message,
                        fontSize = 14.sp,
                        color = Color(0xFF8F4F5F)
                    )

                    userLocation?.let { location ->
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Lat: ${location.latitude}\nLng: ${location.longitude}",
                            fontSize = 12.sp,
                            color = Color(0xFF8F4F5F)
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Button(
                        enabled = userLocation != null,
                        onClick = {
                            val location = userLocation ?: return@Button
                            val uri = Uri.parse(
                                "geo:${location.latitude},${location.longitude}?q=restaurants"
                            )
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE95D7E))
                    ) {
                        Icon(Icons.Filled.Map, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.ENGLISH)
                                "Find restaurants on Maps"
                            else
                                "Најди ресторани на мапа",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(Color(0xFFFFE1E8), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Restaurant,
                            contentDescription = null,
                            tint = Color(0xFFE95D7E)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = if (language == AppLanguage.ENGLISH)
                                "GPS restaurant search"
                            else
                                "GPS пребарување ресторани",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2F3D40)
                        )

                        Text(
                            text = if (language == AppLanguage.ENGLISH)
                                "The app uses your current location and opens real nearby restaurants in Google Maps."
                            else
                                "Апликацијата ја користи твојата моментална локација и отвора реални ресторани во близина преку Google Maps.",
                            fontSize = 13.sp,
                            color = Color(0xFF8F4F5F)
                        )
                    }
                }
            }
        }
    }
}