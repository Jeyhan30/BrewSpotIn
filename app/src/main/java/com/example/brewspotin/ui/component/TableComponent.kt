package com.example.brewspotin.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Enum untuk status meja
enum class TableState {
    AVAILABLE,    // Meja kosong (Putih)
    TAKEN,        // Meja terisi (Merah)
    SELECTED      // Meja yang dipilih pengguna (Hitam)
}

@Composable
fun TableButton(
    modifier: Modifier = Modifier,
    tableState: TableState,
    onClick: () -> Unit
) {
    val backgroundColor = when (tableState) {
        TableState.AVAILABLE -> Color.White
        TableState.TAKEN -> Color.Red // Merah
        TableState.SELECTED -> Color.Black
    }

    val borderColor = when (tableState) {
        TableState.AVAILABLE -> Color.Gray
        TableState.TAKEN -> Color.Transparent // Tidak perlu border jika sudah merah solid
        TableState.SELECTED -> Color.Transparent // Tidak perlu border jika sudah hitam solid
    }

    val clickableModifier = if (tableState == TableState.AVAILABLE) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier // Tidak dapat diklik jika sudah terisi atau sudah dipilih
    }

    Box(
        modifier = modifier
            .size(width = 30.dp, height = 10.dp) // Ukuran default untuk tombol meja
            .clip(RoundedCornerShape(4.dp)) // Sedikit radius pada sudut
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .then(clickableModifier) // Terapkan clickable Modifier di akhir
    ) {
        // Konten opsional di dalam tombol jika diperlukan (misalnya nomor meja)
    }
}


// Preview untuk TableButton
@Preview(showBackground = true)
@Composable
fun TableButtonPreview() {
    Column {
        TableButton(tableState = TableState.AVAILABLE, onClick = {})
        Spacer(Modifier.height(8.dp))
        TableButton(tableState = TableState.TAKEN, onClick = {})
        Spacer(Modifier.height(8.dp))
        TableButton(tableState = TableState.SELECTED, onClick = {})
    }
}