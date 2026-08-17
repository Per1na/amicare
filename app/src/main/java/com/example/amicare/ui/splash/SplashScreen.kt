package com.example.amicare.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.amicare.R
import com.example.amicare.ui.theme.AmicareJonquil
import com.example.amicare.ui.theme.AmicareOrange
import com.example.amicare.ui.theme.AmicareTekhelet
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    // Diubah menjadi menerima parameter boolean (isLoggedIn) untuk logika sesi
    onSplashFinished: (Boolean) -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    // 1. Animasi kemunculan awal (skala membesar & fade-in)
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow
        ),
        label = "scale"
    )

    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearOutSlowInEasing
        ),
        label = "alpha"
    )

    // 2. Animasi putar (Spinning) untuk garis luar
    val infiniteTransition = rememberInfiniteTransition(label = "spinTransition")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotationAngle"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        // Durasi splash (misal 2 detik sudah sangat pas untuk UX)
        delay(3000L)

        // Simulasi pengecekan sesi login lokal (DataStore / SharedPreferences)
        // Untuk saat ini kita set 'false' agar mengarah ke Login
        val isLoggedIn = false

        onSplashFinished(isLoggedIn)
    }

    // Latar belakang utama tetap Tekhelet penuh
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmicareTekhelet),
        contentAlignment = Alignment.Center
    ) {
        // Kontainer luar diperbesar menjadi 300.dp agar ada ruang aman untuk garis tebal
        Box(
            modifier = Modifier
                .size(300.dp)
                .scale(scaleAnim)
                .alpha(alphaAnim),
            contentAlignment = Alignment.Center
        ) {
            // 3. GARIS PUTAR (Canvas ditaruh di belakang agar bagian dalam tertutup rapi)
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                rotate(rotationAngle) {
                    val strokeWidthVal = 16f
                    val whiteCircleRadiusPx = 200.dp.toPx() / 2f
                    val radiusPx = whiteCircleRadiusPx + (strokeWidthVal / 2f)

                    drawCircle(
                        brush = Brush.sweepGradient(
                            listOf(
                                AmicareJonquil,
                                AmicareOrange,
                                Color.Transparent,
                                AmicareJonquil
                            )
                        ),
                        radius = radiusPx,
                        center = center,
                        style = Stroke(
                            width = strokeWidthVal,
                            cap = StrokeCap.Round
                        )
                    )
                }
            }

            // 4. LINGKARAN PUTIH DI TENGAH
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Logo Amicare di dalam lingkaran putih (statis)
                Image(
                    painter = painterResource(id = R.drawable.logo_amicare),
                    contentDescription = "Logo Amicare",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize(0.6f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen { _ -> }
}