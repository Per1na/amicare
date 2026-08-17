package com.example.amicare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.amicare.ui.complaint.ComplaintDetailScreen
import com.example.amicare.ui.complaint.CreateComplaintScreen
import com.example.amicare.ui.history.HistoryScreen
import com.example.amicare.ui.home.HomeScreen
import com.example.amicare.ui.login.LoginScreen
import com.example.amicare.ui.profile.ProfileScreen
import com.example.amicare.ui.splash.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = android.graphics.Color.parseColor("#4A1B9D")
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("splash") }

                    // 1. Tambahkan state untuk menyimpan data pengaduan yang sedang diklik
                    var selectedComplaint by remember { mutableStateOf<com.example.amicare.ui.history.ComplaintModel?>(null) }

                    Crossfade(
                        targetState = currentScreen,
                        animationSpec = tween(durationMillis = 800),
                        label = "ScreenTransition"
                    ) { screen ->
                        when (screen) {
                            "splash" -> {
                                SplashScreen {
                                    window.statusBarColor = android.graphics.Color.TRANSPARENT
                                    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
                                    currentScreen = "login"
                                }
                            }
                            "login" -> {
                                LoginScreen(
                                    onLoginSuccess = {
                                        currentScreen = "home"
                                    }
                                )
                            }
                            "home" -> {
                                HomeScreen(
                                    onNavigateToCreateComplaint = { currentScreen = "create_complaint" },
                                    onNavigateToProfile = { currentScreen = "profile" },
                                    onNavigateToHistory = { currentScreen = "history" }
                                )
                            }
                            "create_complaint" -> {
                                CreateComplaintScreen(
                                    onBack = { currentScreen = "home" },
                                    onSuccessSubmit = {
                                        // Opsional: Bisa langsung diarahkan ke history jika ingin
                                        currentScreen = "history"
                                    }
                                )
                            }
                            "history" -> {
                                HistoryScreen(
                                    onBack = { currentScreen = "home" },
                                    onItemClick = { complaintItem ->
                                        // 2. Simpan item yang diklik ke state, lalu pindah halaman
                                        selectedComplaint = complaintItem
                                        currentScreen = "complaint_detail"
                                    }
                                )
                            }
                            "profile" -> {
                                ProfileScreen(
                                    onBack = { currentScreen = "home" },
                                    onLogout = {
                                        currentScreen = "login"
                                    }
                                )
                            }
                            "complaint_detail" -> {
                                // 3. Kirim data yang tersimpan ke ComplaintDetailScreen
                                selectedComplaint?.let { complaint ->
                                    ComplaintDetailScreen(
                                        complaint = complaint,
                                        onBack = { currentScreen = "history" }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}