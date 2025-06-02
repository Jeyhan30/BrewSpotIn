// File: app/src/main/java/com/example.brewspotin/ui/component/MenuCard.kt
package com.example.brewspotin.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.brewspotin.data.model.MenuItem // <-- Import MenuItem dari lokasi yang benar
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.example.brewspotin.DarkBrown // Asumsi ini ada di theme atau constants

@Composable
fun MenuCard(item: MenuItem, onClick: () -> Unit = {}, onDeleteClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.gambar) // <-- Menggunakan item.imageUrl (String URL)
                        .crossfade(true)
                        .build(),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.deskripsi, // <-- Menggunakan item.description
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Rp ${item.harga}", // <-- Menggunakan item.price (Long) dan format di sini
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkBrown
                    )
                }
            }

            // Tombol Delete di pojok kanan atas
            Text(
                text = "Delete",
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Color.Red)
                    .border(1.dp, Color.Red, RoundedCornerShape(10.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                    .clickable(onClick = onDeleteClick)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuCardPreview() {
    com.example.brewspotin.ui.theme.BrewSpotInTheme {
        MenuCard(
            item = MenuItem(
                id = "m1",
                name = "Coffee Milk",
                deskripsi = "Perpaduan kopi dengan susu yang segar...",
                harga = 25000L, // <-- Gunakan Long untuk preview
                gambar = "https://www.cwcoffee.id/wp-content/uploads/2024/01/coffee-milk-768x768.jpg"
            )
        )
    }
}