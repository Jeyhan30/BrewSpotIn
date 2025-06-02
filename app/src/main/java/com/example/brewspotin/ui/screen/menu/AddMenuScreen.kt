// File: app/src/main/java/com/example.brewspotin/ui/screen/menu/AddMenuScreen.kt
package com.example.brewspotin.ui.screen.menu // <-- Pastikan package ini

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.brewspotin.data.model.MenuItem // <-- Import MenuItem dari lokasi yang benar
import com.example.brewspotin.DarkBrown
import com.example.brewspotin.MediumGrayBorder
import com.example.brewspotin.ui.screen.menu.MenuViewModel // <-- Import MenuViewModel
import com.example.brewspotin.ui.screen.reservation.AppBarBrown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMenuScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: MenuViewModel = viewModel()
) {
    val context = LocalContext.current
    var cafeName by remember { mutableStateOf("Jokopi") }
    var menuName by remember { mutableStateOf("") }
    // ********** PERUBAHAN DI SINI **********
    var priceText by remember { mutableStateOf(TextFieldValue("Rp ")) } // TextFieldValue untuk input teks harga
    // ****************************************
    var imageUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val uiMessage by viewModel.uiMessage.collectAsState()

    LaunchedEffect(uiMessage) {
        uiMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearUiMessage()
        }
    }

    Scaffold(
        topBar = {
            // ********** PERBAIKAN BENTUK TOPBAR (Konsisten dengan SelectTableScreen) **********
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp) // Tinggi yang cukup untuk TopAppBar
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp
                        )
                    )
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 24.dp,
                            bottomEnd = 24.dp
                        )
                    )
                    .background(com.example.brewspotin.ui.screen.reservation.AppBarBrown) // Gunakan AppBarBrown dari package reservation
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Tambah Menu Cafe",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
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
                        Spacer(modifier = Modifier.width(48.dp)) // Untuk menyeimbangkan navigationIcon
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
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
                        if (menuName.isBlank()) {
                            Toast.makeText(context, "Nama menu tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        // ********** PERUBAHAN DI SINI **********
                        // Ambil harga sebagai Long dari teks input
                        val parsedPrice = priceText.text.replace("Rp ", "").replace(".", "").toLongOrNull() ?: 0L

                        val newMenuItem = MenuItem(
                            // ID akan otomatis dibuat oleh Firestore saat add()
                            name = menuName.trim(),
                            deskripsi = description.trim(),
                            harga = parsedPrice, // <-- Kirim sebagai Long
                            gambar = imageUrl.trim()
                        )
                        // ****************************************
                        viewModel.addMenuItem(newMenuItem) { success, errorMessage ->
                            if (success) {
                                menuName = ""
                                priceText = TextFieldValue("Rp ") // Reset TextFieldValue
                                imageUrl = ""
                                description = ""
                                onNavigateBack()
                            } else {
                                // Toast sudah dihandle oleh ViewModel
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBrown),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Upload !",
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
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // Cafe
            Text(
                text = "Cafe",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = cafeName,
                onValueChange = { /* Read-only */ },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = DarkBrown,
                    unfocusedBorderColor = MediumGrayBorder
                ),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                placeholder = { Text("Jokopi") }
            )

            // Nama Menu
            Text(
                text = "Nama Menu",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = menuName,
                onValueChange = { menuName = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = DarkBrown,
                    unfocusedBorderColor = MediumGrayBorder
                ),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                placeholder = { Text("Popcorn Caramel") }
            )

            // Harga dan Upload Gambar Menu (dalam satu Row)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Harga",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = priceText, // <-- Menggunakan priceText
                        onValueChange = { newValue ->
                            val currentText = priceText.text
                            val newText = newValue.text

                            val filteredText = if (newText.startsWith("Rp ")) {
                                newText.filterIndexed { index, char ->
                                    index < 3 || char.isDigit() || char == '.' // Izinkan titik juga jika ada di harga
                                }
                            } else {
                                "Rp " + newText.filter { it.isDigit() || it == '.' }
                            }

                            val finalFormattedText = if (filteredText.length > 15) filteredText.substring(0, 15) else filteredText
                            val newSelection = if (finalFormattedText.length < 3) {
                                finalFormattedText.length
                            } else if (newValue.selection.start < 3 && newText.length >= 3) {
                                3
                            } else {
                                finalFormattedText.length
                            }

                            priceText = TextFieldValue(
                                text = finalFormattedText,
                                selection = androidx.compose.ui.text.TextRange(newSelection)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = DarkBrown,
                            unfocusedBorderColor = MediumGrayBorder
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        placeholder = { Text("Rp 23.000") }
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Upload Gambar Menu",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = DarkBrown,
                            unfocusedBorderColor = MediumGrayBorder
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        placeholder = {
                            Text(
                                "http://link_your_image.jpg",
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Deskripsi Menu
            Text(
                text = "Deskripsi Menu",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = DarkBrown,
                    unfocusedBorderColor = MediumGrayBorder
                ),
                shape = RoundedCornerShape(8.dp),
                placeholder = { Text("Perpaduan kopi dengan tambahan rasa popcorn caramel yang menambah cita rasa manis dari minuman ini.") }
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddMenuScreenPreview() {
    com.example.brewspotin.ui.theme.BrewSpotInTheme {
        AddMenuScreen()
    }
}