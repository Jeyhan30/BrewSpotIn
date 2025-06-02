//package com.example.brewspotin.ui.screen.reservation
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions // Untuk keyboard type
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.CheckCircle
//import androidx.compose.material.icons.filled.DateRange
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material.icons.filled.ShoppingCart
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType // Untuk keyboard type
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.brewspotin.R
//
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ReservationScreen(
//    onNavigateToSelectTable: () -> Unit
//) {
//
//    val context = LocalContext.current
//    var namaAnda by remember { mutableStateOf("") }
//    var selectedDate by remember { mutableStateOf("22/05/2025") } // Default value untuk tanggal
//    var totalTamu by remember { mutableStateOf("2") }
//    var selectedTime by remember { mutableStateOf("12.00") } // Default value untuk waktu, ubah format
//
//    val timeOptions = remember {
//        (0..23).map { hour ->
//            String.format("%02d.00", hour)
//        }
//    }
//    val isFormValid = namaAnda.isNotBlank() &&
//            selectedDate.isNotBlank() &&
//            selectedTime.isNotBlank()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//            .padding(bottom = 16.dp)
//    ) {
//        // Header
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(250.dp)
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.jokopi),
//                contentDescription = "Cafe Background",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//            )
//
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .offset(y = 215.dp),
//                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(1.dp)
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.cafe_image),
//                        contentDescription = "Cafe Logo",
//                        modifier = Modifier
//                            .size(160.dp)
//                            .clip(RoundedCornerShape(8.dp))
//                    )
//                    Column {
//                        Spacer(modifier = Modifier.height(50.dp))
//                        Text("JOKOPI", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = DarkBrown)
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
//                            Spacer(modifier = Modifier.width(4.dp))
//                            Text("Jl. Jakarta No.26", fontSize = 14.sp, color = Color.Gray)
//                        }
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
//                            Spacer(modifier = Modifier.width(4.dp))
//                            Text("Buka 24 jam", fontSize = 14.sp, color = Color.Gray)
//                        }
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
//                            Spacer(modifier = Modifier.width(4.dp))
//                            Text("Rp 20.000 - 25.000", fontSize = 14.sp, color = Color.Gray)
//                        }
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(140.dp))
//
//        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
//            Text(
//                text = "Reservasi Sekarang!",
//                fontSize = 22.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.Black,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//
//            // Nama
//            Text("Nama Anda", fontSize = 16.sp, color = Color.Black)
//            OutlinedTextField(
//                value = namaAnda,
//                onValueChange = { namaAnda = it },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 16.dp)
//                    .clip(RoundedCornerShape(8.dp)),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    focusedTextColor = Color.Black,
//                    unfocusedTextColor = Color.Black,
//                    containerColor = Color.White,
//                    focusedBorderColor = DarkBrown,
//                    unfocusedBorderColor = MediumGrayBorder
//                ),
//                singleLine = true,
//                shape = RoundedCornerShape(8.dp),
//                placeholder = { Text("Lukas Tri") }
//            )
//
//            // Row Tanggal dan Total Tamu
//            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//                Column(modifier = Modifier.weight(1f)) {
//                    Text("Tanggal", fontSize = 16.sp, color = Color.Black)
//                    OutlinedTextField(
//                        value = selectedDate,
//                        onValueChange = { newValue ->
//                            selectedDate = formatAndValidateDate(newValue) // <-- Panggil fungsi format
//                        },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // <-- Hanya angka
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clip(RoundedCornerShape(8.dp)),
//                        trailingIcon = {
//                            Icon(Icons.Default.DateRange, contentDescription = "Pilih Tanggal")
//                        },
//                        colors = TextFieldDefaults.outlinedTextFieldColors(
//                            focusedTextColor = Color.Black,
//                            unfocusedTextColor = Color.Black,
//                            containerColor = Color.White,
//                            focusedBorderColor = DarkBrown,
//                            unfocusedBorderColor = MediumGrayBorder
//                        ),
//                        singleLine = true
//                    )
//                }
//
//                Column(modifier = Modifier.weight(1f)) {
//                    Text("Total Tamu", fontSize = 16.sp, color = Color.Black)
//                    var expanded by remember { mutableStateOf(false) }
//                    val options = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10+")
//                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
//                        OutlinedTextField(
//                            value = totalTamu,
//                            onValueChange = {},
//                            readOnly = true,
//                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                            modifier = Modifier
//                                .menuAnchor()
//                                .fillMaxWidth()
//                                .clip(RoundedCornerShape(8.dp)),
//                            colors = TextFieldDefaults.outlinedTextFieldColors(
//                                focusedTextColor = Color.Black,
//                                unfocusedTextColor = Color.Black,
//                                containerColor = Color.White,
//                                focusedBorderColor = DarkBrown,
//                                unfocusedBorderColor = MediumGrayBorder
//                            ),
//                            singleLine = true
//                        )
//                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                            options.forEach {
//                                DropdownMenuItem(text = { Text(it) }, onClick = {
//                                    totalTamu = it
//                                    expanded = false
//                                })
//                            }
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Waktu (Dropdown seperti Total Tamu)
//            Text(
//                text = "Waktu",
//                fontSize = 16.sp,
//                color = Color.Black,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//            var expandedTime by remember { mutableStateOf(false) } // State untuk dropdown waktu
//            ExposedDropdownMenuBox(
//                expanded = expandedTime,
//                onExpandedChange = { expandedTime = !expandedTime },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(8.dp))
//            ) {
//                OutlinedTextField(
//                    value = selectedTime,
//                    onValueChange = { }, // Tidak bisa diketik langsung
//                    readOnly = true, // Hanya bisa dipilih dari dropdown
//                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTime) },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .menuAnchor()
//                        .clip(RoundedCornerShape(8.dp)),
//                    colors = TextFieldDefaults.outlinedTextFieldColors(
//                        focusedTextColor = Color.Black,
//                        unfocusedTextColor = Color.Black,
//                        containerColor = Color.White,
//                        focusedBorderColor = DarkBrown,
//                        unfocusedBorderColor = MediumGrayBorder
//                    ),
//                    singleLine = true,
//                    shape = RoundedCornerShape(8.dp)
//                )
//                ExposedDropdownMenu(
//                    expanded = expandedTime,
//                    onDismissRequest = { expandedTime = false }
//                ) {
//                    timeOptions.forEach { hourOption ->
//                        DropdownMenuItem(
//                            text = { Text(hourOption) },
//                            onClick = {
//                                selectedTime = hourOption
//                                expandedTime = false
//                            }
//                        )
//                    }
//                }
//            }
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // Tombol Selanjutnya
//            Button(
//                onClick = onNavigateToSelectTable,
//                enabled = isFormValid,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp)
//                    .clip(RoundedCornerShape(10.dp)),
//                colors = ButtonDefaults.buttonColors(containerColor = DarkBrown),
//                shape = RoundedCornerShape(10.dp)
//            ) {
//                Text(
//                    text = "Selanjutnya",
//                    color = Color.White,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//        }
//    }
//}
//
//// Fungsi Helper untuk format tanggal otomatis
//fun formatAndValidateDate(input: String): String {
//    var formatted = input.replace("/", "") // Hapus semua slash yang sudah ada
//
//    // Batasi input maksimal 8 digit (ddMMyyyy)
//    if (formatted.length > 8) {
//        formatted = formatted.substring(0, 8)
//    }
//
//    val sb = StringBuilder()
//    for (i in formatted.indices) {
//        if (i > 0 && (i == 2 || i == 4)) { // Tambahkan '/' setelah 2 digit (hari) dan 4 digit (bulan)
//            sb.append('/')
//        }
//        sb.append(formatted[i])
//    }
//
//    return sb.toString()
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun ReservationScreenPreview() {
//    ReservationScreen(onNavigateToSelectTable = {})
//}