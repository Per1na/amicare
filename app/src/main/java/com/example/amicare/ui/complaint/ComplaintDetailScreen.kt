package com.example.amicare.ui.complaint

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amicare.ui.history.ComplaintModel // Pastikan import model dari package history
import com.example.amicare.ui.theme.AmicareJonquil
import com.example.amicare.ui.theme.AmicareTekhelet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintDetailScreen(
    complaint: ComplaintModel, // Menerima data dinamis dari item yang diklik
    onBack: () -> Unit = {}
) {
    // Menyesuaikan warna berdasarkan status dinamis
    val statusColor = when (complaint.status) {
        "Disetujui" -> Color(0xFF4CAF50)
        "Diproses" -> AmicareJonquil
        "Diajukan" -> Color(0xFF2196F3)
        "Ditolak" -> Color(0xFFE53935)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.statusBars.add(WindowInsets(top = 12.dp)),
                title = { Text("Detail Pengaduan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card Utama: Status & Tanggal
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Tanggal Laporan",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = complaint.date, // Menggunakan tanggal dinamis
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = statusColor.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = complaint.status, // Menggunakan status dinamis
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }
                }
            }

            // Card Informasi: Kategori & Judul
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = complaint.category.uppercase(), // Menggunakan kategori dinamis
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmicareTekhelet
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = complaint.title, // Menggunakan judul dinamis
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Deskripsi Pengaduan:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = complaint.description, // Menggunakan deskripsi dinamis
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 20.sp
                    )
                }
            }

            // Card Tanggapan dari Kampus / Pengelola
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AmicareTekhelet.copy(alpha = 0.05f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(AmicareTekhelet)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tanggapan Pengelola Kampus",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmicareTekhelet
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (!complaint.admin_response.isNullOrEmpty()) complaint.admin_response else "Belum ada tanggapan dari pengelola.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

// Preview menggunakan data dummy ComplaintModel
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ComplaintDetailScreenPreview() {
    val sampleComplaint = ComplaintModel(
        id = "1",
        title = "Kerusakan Fasilitas Lab Komputer Gedung D",
        category = "Sarana & Prasarana",
        description = "Pelapor melaporkan adanya 3 unit PC di Lab Komputer Gedung D yang mengalami kerusakan pada bagian power supply.",
        status = "Diproses",
        admin_response = "Laporan telah diteruskan ke teknisi.",
        date = "15 Agu 2026"
    )

    MaterialTheme {
        ComplaintDetailScreen(complaint = sampleComplaint)
    }
}