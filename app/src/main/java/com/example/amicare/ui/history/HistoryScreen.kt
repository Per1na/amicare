package com.example.amicare.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amicare.data.network.RetrofitClient
import com.example.amicare.ui.theme.AmicareJonquil
import com.example.amicare.ui.theme.AmicareTekhelet

// Model data lokal untuk UI
data class ComplaintModel(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val admin_response: String?,
    val status: String, // Diajukan, Diproses, Disetujui, Ditolak
    val date: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBack: () -> Unit = {},
    onItemClick: (ComplaintModel) -> Unit = {}
) {
    var allComplaints by remember { mutableStateOf<List<ComplaintModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Pilihan Filter Chip lengkap 5 opsi
    val filterOptions = listOf("Semua", "Diajukan", "Diproses", "Disetujui", "Ditolak")
    var selectedFilter by remember { mutableStateOf("Semua") }

    // Mengambil data dari API Laravel saat layar pertama kali dibuka
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.apiService.getComplaints()
            if (response.success) {
                // Memetakan data dari backend (Complaint) ke ComplaintModel UI
                allComplaints = response.data.map { item ->
                    ComplaintModel(
                        id = item.id.toString(),
                        title = item.title,
                        category = item.category,
                        description = item.description,
                        admin_response = item.admin_response,
                        status = item.status,
                        date = item.created_at.take(10) // Mengambil format tanggal dasar
                    )
                }
            } else {
                errorMessage = "Gagal memuat data: ${response.message}"
            }
        } catch (e: Exception) {
            errorMessage = "Terjadi kesalahan: Pastikan server Laravel aktif. (${e.localizedMessage})"
        } finally {
            isLoading = false
        }
    }

    // Logika penyaringan data berdasarkan chip yang dipilih
    val filteredList = if (selectedFilter == "Semua") {
        allComplaints
    } else {
        allComplaints.filter { it.status.equals(selectedFilter, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.statusBars.add(WindowInsets(top = 12.dp)),
                title = { Text("Riwayat Pengaduan", fontWeight = FontWeight.Bold) },
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
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Baris filter chip horizontal
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AmicareTekhelet,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Konten utama berdasarkan status koneksi & data
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AmicareTekhelet)
                    }
                }
                errorMessage != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorMessage ?: "Terjadi kesalahan",
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }
                }
                filteredList.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ada riwayat dengan status '$selectedFilter'",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredList) { item ->
                            HistoryCardItem(
                                item = item,
                                onClick = { onItemClick(item) }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryCardItem(
    item: ComplaintModel,
    onClick: () -> Unit
) {
    val statusColor = when (item.status) {
        "Disetujui" -> Color(0xFF4CAF50) // Hijau
        "Diproses" -> AmicareJonquil    // Kuning/Emas
        "Diajukan" -> Color(0xFF2196F3) // Biru
        "Ditolak" -> Color(0xFFE53935)  // Merah
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.category,
                    fontSize = 12.sp,
                    color = AmicareTekhelet,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusColor.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = item.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.date,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HistoryScreenPreview() {
    MaterialTheme {
        HistoryScreen()
    }
}