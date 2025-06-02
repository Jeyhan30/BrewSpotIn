// File: app/src/main/java/com/example.brewspotin/ui/screen/update/SelectTableScreen.kt
package com.example.brewspotin.ui.screen.reservation // <-- Perhatikan package ini

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.brewspotin.R
import com.example.brewspotin.ui.component.TableButton
import com.example.brewspotin.ui.component.TableState
import com.example.brewspotin.data.model.TableOrigin
import com.example.brewspotin.ui.screen.history.ReservationAndHistoryProcessorViewModel

val DarkBrown = Color(0xFF6B4226)
val AppBarBrown = Color(0xFF5D4037)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTableScreen(
    onNavigateBack: () -> Unit,
    onNavigateNext: (selectedTableId: String?) -> Unit = {},
    tableViewModel: TableViewModel = viewModel(),
    reservationProcessorViewModel: ReservationAndHistoryProcessorViewModel = viewModel()
) {
    val context = LocalContext.current
    val tableStatuses by tableViewModel.tableStatuses.collectAsState()
    val autoTakenTableIds by reservationProcessorViewModel.autoTakenTableIds.collectAsState()
    val uiMessage by tableViewModel.uiMessage.collectAsState()

    // Tampilkan Toast jika ada pesan UI dari TableViewModel
    LaunchedEffect(uiMessage) {
        uiMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            tableViewModel.clearUiMessage()
        }
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
                            text = "Pemilihan Meja",
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
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        onNavigateNext(null) // Tidak perlu membawa ID meja yang dipilih saat selesai update
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBrown),
                    shape = RoundedCornerShape(10.dp),
                    enabled = true
                ) {
                    Text(
                        text = "Selesai Update Meja",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Pilih Meja",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            Text(
                text = "Klik kotak putih untuk memilih meja Anda!",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFebd3b0))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.denah_cafe),
                    contentDescription = "Denah Cafe",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(width = 330.dp, height = 450.dp)
                        .align(alignment = Alignment.Center).padding(12.dp)
                )

                // Logika untuk menentukan TableState yang sebenarnya untuk setiap tombol di UI
                val getActualTableState: (String) -> TableState = { tableId ->
                    val currentTableStatus = tableStatuses.find { it.id == tableId }
                    val isCurrentlyAutoTaken = autoTakenTableIds.contains(tableId)

                    when {
                        // PRIORITAS 1: Meja TAKEN manual oleh admin (paling tinggi)
                        currentTableStatus?.status == "TAKEN" && currentTableStatus.statusOrigin == TableOrigin.ADMIN_MANUAL.name ->
                            TableState.TAKEN // Merah (TAKEN manual)
                        // PRIORITAS 2: Meja TAKEN oleh reservasi otomatis (jika tidak di-override admin)
                        isCurrentlyAutoTaken ->
                            TableState.SELECTED // Hitam (TAKEN otomatis)
                        // PRIORITAS 3: Meja AVAILABLE di DB
                        currentTableStatus?.status == "AVAILABLE" ->
                            TableState.AVAILABLE // Putih
                        // Default jika tidak ada di DB, anggap AVAILABLE
                        else -> TableState.AVAILABLE
                    }
                }
                // ********** PERBAIKAN LOGIKA onTableClick **********
                val onTableClick: (String) -> Unit = { id ->
                    val currentActualUiState = getActualTableState(id) // Dapatkan status UI saat ini

                    // Tentukan status BARU yang akan dikirim ke ViewModel/DB
                    val newTargetStatusString: String = when (currentActualUiState) {
                        // Jika saat ini terlihat AVAILABLE (putih), admin mau membuatnya TAKEN di DB
                        TableState.AVAILABLE -> "TAKEN"
                        // Jika saat ini terlihat TAKEN (merah) atau SELECTED (hitam), admin mau membuatnya AVAILABLE di DB
                        TableState.TAKEN, TableState.SELECTED -> "AVAILABLE"
                    }
                    tableViewModel.updateTableStatus(id, newTargetStatusString)
                }
                // ****************************************************


                // ********** Penempatan Meja (TableButton) - Posisi TETAP SAMA **********
                // Pastikan semua TableButton memanggil onTableClick(ID) dan getActualTableState(ID)

                TableButton(
                    modifier = Modifier.offset(x = 85.dp, y = 25.dp)
                        .size(width = 65.dp, height = 20.dp).clip(
                            RoundedCornerShape(12.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(12.dp)),
                    tableState = getActualTableState("T1"),
                    onClick = { onTableClick("T1") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 85.dp, y = 53.dp)
                        .size(width = 65.dp, height = 20.dp).clip(
                            RoundedCornerShape(12.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(12.dp)),
                    tableState = getActualTableState("T2"),
                    onClick = { onTableClick("T2") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 160.dp, y = 105.dp)
                        .size(width = 17.dp, height = 38.dp).clip(
                            RoundedCornerShape(12.dp)
                        ),
                    tableState = getActualTableState("T3"),
                    onClick = { onTableClick("T3") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 215.dp, y = 105.dp)
                        .size(width = 17.dp, height = 38.dp).clip(
                            RoundedCornerShape(12.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(12.dp)),
                    tableState = getActualTableState("T4"),
                    onClick = { onTableClick("T4") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 240.dp, y = 105.dp)
                        .size(width = 17.dp, height = 38.dp).clip(
                            RoundedCornerShape(12.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(12.dp)),
                    tableState = getActualTableState("T5"),
                    onClick = { onTableClick("T5") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 265.dp, y = 105.dp)
                        .size(width = 17.dp, height = 38.dp).clip(
                            RoundedCornerShape(12.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(12.dp)),
                    tableState = getActualTableState("T6"),
                    onClick = { onTableClick("T6") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 290.dp, y = 105.dp)
                        .size(width = 17.dp, height = 38.dp).clip(
                            RoundedCornerShape(12.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(12.dp)),
                    tableState = getActualTableState("T7"),
                    onClick = { onTableClick("T7") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 78.dp, y = 175.dp)
                        .size(width = 35.dp, height = 25.dp).clip(
                            RoundedCornerShape(10.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                    tableState = getActualTableState("T8"),
                    onClick = { onTableClick("T8") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 115.dp, y = 175.dp)
                        .size(width = 35.dp, height = 25.dp).clip(
                            RoundedCornerShape(10.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                    tableState = getActualTableState("T9"),
                    onClick = { onTableClick("T9") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 78.dp, y = 205.dp)
                        .size(width = 35.dp, height = 25.dp).clip(
                            RoundedCornerShape(10.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                    tableState = getActualTableState("T10"),
                    onClick = { onTableClick("T10") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 115.dp, y = 205.dp)
                        .size(width = 35.dp, height = 25.dp).clip(
                            RoundedCornerShape(10.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                    tableState = getActualTableState("T11"),
                    onClick = { onTableClick("T11") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 78.dp, y = 235.dp)
                        .size(width = 35.dp, height = 25.dp).clip(
                            RoundedCornerShape(10.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                    tableState = getActualTableState("T12"),
                    onClick = { onTableClick("T12") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 115.dp, y = 235.dp)
                        .size(width = 35.dp, height = 25.dp).clip(
                            RoundedCornerShape(10.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                    tableState = getActualTableState("T13"),
                    onClick = { onTableClick("T13") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 78.dp, y = 285.dp)
                        .size(width = 35.dp, height = 25.dp).clip(
                            RoundedCornerShape(10.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                    tableState = getActualTableState("T14"),
                    onClick = { onTableClick("T14") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 115.dp, y = 285.dp)
                        .size(width = 35.dp, height = 25.dp).clip(
                            RoundedCornerShape(10.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                    tableState = getActualTableState("T15"),
                    onClick = { onTableClick("T15") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 225.dp, y = 285.dp)
                        .size(width = 35.dp, height = 25.dp).clip(
                            RoundedCornerShape(10.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                    tableState = getActualTableState("T16"),
                    onClick = { onTableClick("T16") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 262.dp, y = 285.dp)
                        .size(width = 35.dp, height = 25.dp).clip(
                            RoundedCornerShape(10.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                    tableState = getActualTableState("T17"),
                    onClick = { onTableClick("T17") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 255.dp, y = 155.dp)
                        .size(width = 17.dp, height = 38.dp).clip(
                            RoundedCornerShape(12.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(12.dp)),
                    tableState = getActualTableState("T18"),
                    onClick = { onTableClick("T18") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 280.dp, y = 155.dp)
                        .size(width = 17.dp, height = 38.dp).clip(
                            RoundedCornerShape(12.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(12.dp)),
                    tableState = getActualTableState("T19"),
                    onClick = { onTableClick("T19") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 165.dp, y = 30.dp)
                        .size(width = 145.dp, height = 25.dp).clip(
                            RoundedCornerShape(10.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                    tableState = getActualTableState("T20"),
                    onClick = { onTableClick("T20") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 200.dp, y = 65.dp)
                        .size(width = 35.dp, height = 25.dp).clip(
                            RoundedCornerShape(10.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                    tableState = getActualTableState("T21"),
                    onClick = { onTableClick("T21") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 238.dp, y = 65.dp)
                        .size(width = 35.dp, height = 25.dp).clip(
                            RoundedCornerShape(10.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                    tableState = getActualTableState("T22"),
                    onClick = { onTableClick("T22") }
                )
                TableButton(
                    modifier = Modifier.offset(x = 278.dp, y = 65.dp)
                        .size(width = 35.dp, height = 25.dp).clip(
                            RoundedCornerShape(10.dp)
                        ).border(1.dp, color = Color.Gray, RoundedCornerShape(10.dp)),
                    tableState = getActualTableState("T23"),
                    onClick = { onTableClick("T23") }
                )

                // Text untuk Area Luar (di gambar)
                Text(
                    text = "AREA LUAR",
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(x = 205.dp, y = 10.dp)
                )
                Text(
                    text = "PHOTO\nBOOTH",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(x = 93.dp, y = 100.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "KASIR",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(x = 255.dp, y = 230.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "TEMPAT\nPARKIR",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(x = 245.dp, y = 350.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "TEMPAT\nPARKIR",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(x = 55.dp, y = 350.dp),
                    textAlign = TextAlign.Center
                )

            }

            Spacer(modifier = Modifier.height(24.dp))

            // Keterangan
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Keterangan:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LegendItem(color = Color.White, label = "Meja kosong", hasBorder = true)
                LegendItem(
                    color = Color.Black,
                    label = "Meja yang dipilih"
                ) // Ini akan menjadi TAKEN dari reservasi
                LegendItem(
                    color = Color(0xFFD32F2F),
                    label = "Meja terisi"
                ) // Ini akan menjadi TAKEN manual
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

    @Composable
    fun LegendItem(color: Color, label: String, hasBorder: Boolean = false) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(color, CircleShape)
                    .then(
                        if (hasBorder) Modifier.border(
                            1.dp,
                            Color.Gray,
                            CircleShape
                        ) else Modifier
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontSize = 14.sp, color = Color.Black)
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun SelectTableScreenPreview() {
        com.example.brewspotin.ui.theme.BrewSpotInTheme {
            SelectTableScreen(
                onNavigateBack = {},
                onNavigateNext = {}
            )
        }
    }