package com.example.brewspotin.ui.screen.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add // Untuk ikon tambah menu
import androidx.compose.material.icons.filled.AccessTime // Untuk ikon riwayat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brewspotin.R // Pastikan ini mengacu ke R proyekmu
import com.example.brewspotin.DarkBrown // Import DarkBrown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onNavigateToHistory: () -> Unit = {},
    onNavigateToAddMenu: () -> Unit = {},
    onNavigateToUpdateTable: () -> Unit = {}
) {
    Scaffold(
        // Tanpa TopAppBar atau BottomBar bawaan Scaffold, karena kita akan buat kustom di Box
    ) { paddingValues ->
        // Box utama untuk background dan konten
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Penting untuk penyesuaian padding jika ada Scaffold di level lebih tinggi
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.background_home), // Ganti dengan ID gambar background kamu
                contentDescription = "Background Home",
                contentScale = ContentScale.Crop, // Crop agar mengisi seluruh area
                modifier = Modifier.fillMaxSize()
            )

            // Overlay semi-transparan untuk readability (opsional, jika teks tidak terbaca)
             Box(
                 modifier = Modifier
                     .fillMaxSize()
                     .background(Color.White.copy(alpha = 0.8f))
             )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp) // Padding horizontal untuk semua konten
                    .padding(top = 10.dp), // Padding atas untuk menjauh dari status bar
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top // Konten dimulai dari atas
            ) {
                // Gambar Kaligrafi "Welcome to BrewSpot"
                Image(
                    painter = painterResource(id = R.drawable.to), // Ganti dengan ID gambar kaligrafi kamu
                    contentDescription = "Welcome to BrewSpot",
                    modifier = Modifier
                        .size(300.dp)
                        .padding(bottom = 32.dp)
                )

                // Row untuk 2 Card Navigasi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround, // Jarak antar card
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.height(250.dp))
                    // Card: Reservation History
                    AdminFeatureCard(
                        title = "Reservation History",
                        icon = Icons.Default.AccessTime, // Ikon jam/riwayat
                        imageRes = R.drawable.table, // Gambar untuk card riwayat
                        onClick = onNavigateToHistory
                    )

                    Spacer(modifier = Modifier.width(16.dp)) // Jarak antar card

                    // Card: Add Menu
                    AdminFeatureCard(
                        title = "ADD MENU",
                        icon = Icons.Default.Add, // Ikon tambah
                        imageRes = R.drawable.add_menu, // Gambar untuk card add menu
                        onClick = onNavigateToAddMenu
                    )
                }

                Spacer(modifier = Modifier.weight(1f)) // Dorong button Update Table ke bawah

                // Button: Update Table
                Button(
                    onClick = onNavigateToUpdateTable,
                    modifier = Modifier
                        .fillMaxWidth() // Lebar 90% dari parent Column
                        .padding(bottom = 32.dp) // Padding dari bawah layar
                        .clip(RoundedCornerShape(12.dp)) // Sudut membulat
                        .shadow(4.dp, RoundedCornerShape(12.dp)), // Efek shadow
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBrown) // Warna coklat gelap
                ) {
                    Text(
                        text = "Update Table",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AdminFeatureCard(
    title: String,
    icon: ImageVector,
    imageRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp) // Lebar Card
            .height(200.dp) // Tinggi Card
            .shadow(6.dp, RoundedCornerShape(12.dp)) // Shadow
            .clip(RoundedCornerShape(12.dp)) // Clip untuk bentuk
            .background(Color.White) // Background putih
            .clickable(onClick = onClick), // Card dapat diklik
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF263238)) // Warna latar belakang card (dark gray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Gambar di dalam Card
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp) // Ukuran gambar di Card
                    .clip(RoundedCornerShape(8.dp))
                    .padding(bottom = 8.dp)
            )
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp) // Ukuran ikon
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminDashboardScreenPreview() {
    com.example.brewspotin.ui.theme.BrewSpotInTheme {
        AdminDashboardScreen()
    }
}