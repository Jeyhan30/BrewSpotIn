// File: app/src/main/java/com.example.brewspotin/ui/screen/history/HistoryListScreen.kt
package com.example.brewspotin.ui.screen.history

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.brewspotin.ui.component.BookingHistoryCard
import com.example.brewspotin.DarkBrown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryListScreen( // Nama tetap HistoryListScreen
    onNavigateBack: () -> Unit = {},
    viewModel: AdminHistoryListViewModel = viewModel() // <-- Injeksi AdminHistoryListViewModel
) {
    val bookingHistoryList by viewModel.bookingHistory.collectAsState()
    val uiMessage by viewModel.uiMessage.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(uiMessage) {
        uiMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearUiMessage()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadAdminBookingHistory() // <-- Memanggil fungsi load yang benar
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight().height(80.dp) // Tinggi disesuaikan dengan konten TopAppBar
                    .padding(bottom = 0.dp) // Sesuaikan padding bawah jika perlu
                    .shadow(
                        elevation = 8.dp, // Intensitas bayangan
                        shape = RoundedCornerShape(
                            bottomStart = 24.dp, // Radius sudut bawah kiri
                            bottomEnd = 24.dp // Radius sudut bawah kanan
                        )
                    )
                    .background(DarkBrown) // Latar belakang TopAppBar yang melengkung
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Riwayat Reservasi",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        // Spacer dengan lebar yang sama dengan IconButton (48.dp default untuk icon)
                        Spacer(modifier = Modifier.width(40.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent) // Tetap transparan agar background Box terlihat
                )
            }

        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(bookingHistoryList) { item ->
                BookingHistoryCard(item = item)
            }
        }
    }
}