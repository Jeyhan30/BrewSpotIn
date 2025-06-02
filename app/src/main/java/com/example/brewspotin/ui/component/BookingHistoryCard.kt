// File: app/src/main/java/com.example.brewspotin/ui/component/BookingHistoryCard.kt
package com.example.brewspotin.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TableRestaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brewspotin.DarkBrown
import com.example.brewspotin.data.model.BookingHistoryItem // <-- Import ini

@Composable
fun BookingHistoryCard(item: BookingHistoryItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
                    text = item.dateIn, // <-- Menggunakan dateIn
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .border(1.dp, color = DarkBrown, RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = item.reservId, // <-- Menggunakan reservId
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold, // Tambah fontWeight
                        maxLines = 1,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 5.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total Pembayaran: ${item.totalPayment}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBrown,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(Icons.Default.Person, contentDescription = "Nama", tint = Color.Gray, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.nameIn, fontSize = 14.sp, color = Color.Black) // <-- Menggunakan nameIn
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(Icons.Default.AccessTime, contentDescription = "Waktu", tint = Color.Gray, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.timeIn, fontSize = 14.sp, color = Color.Black) // <-- Menggunakan timeIn
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Icon(Icons.Default.People, contentDescription = "Jumlah Tamu", tint = Color.Gray, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${item.guestIn} orang", fontSize = 14.sp, color = Color.Black) // <-- Menggunakan guestIn
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.TableRestaurant, contentDescription = "Meja", tint = Color.Gray, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.tableIn, fontSize = 14.sp, color = Color.Black) // <-- Menggunakan tableIn
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingHistoryCardPreview() {
    com.example.brewspotin.ui.theme.BrewSpotInTheme {
        BookingHistoryCard(
            item = BookingHistoryItem(
                reservId = "12345678901234567890", // Contoh ID reservasi
                adminHistoryRecordId = "someAdminHistoryId",
                dateIn = "02 Juni 2025",
                timeIn = "12.00 AM",
                totalPayment = "Rp 30.000",
                nameIn = "Lukas Tri",
                guestIn = 2,
                tableIn = "T10"
            )
        )
    }
}