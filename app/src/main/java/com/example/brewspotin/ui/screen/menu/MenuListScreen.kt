// File: app/src/main/java/com/example.brewspotin/ui/screen/menu/MenuListScreen.kt
package com.example.brewspotin.ui.screen.menu // <-- Pastikan package ini

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.brewspotin.R
import com.example.brewspotin.ui.component.MenuCard
import com.example.brewspotin.DarkBrown

@Composable
fun MenuListScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToAddMenu: () -> Unit = {},
    viewModel: MenuViewModel = viewModel()
) {
    val menuItems by viewModel.menuItems.collectAsState()
    val uiMessage by viewModel.uiMessage.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(uiMessage) {
        uiMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearUiMessage()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadMenuItems()
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Gray)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.jokopi_menu_header),
                    contentDescription = "Cafe Menu Header",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 16.dp, start = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }

                Text(
                    text = "JOKOPI",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 16.dp)
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
                    onClick = onNavigateToAddMenu,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBrown),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Tambah Menu",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Menu",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(menuItems) { item ->
                MenuCard(item = item, onClick = {
                    println("Menu ${item.name} diklik!")
                }, onDeleteClick = {
                    viewModel.deleteMenuItem(item.id) { success, errorMessage ->
                        if (!success) {
                            // Toast akan ditampilkan oleh ViewModel
                        }
                    }
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuListScreenPreview() {
    com.example.brewspotin.ui.theme.BrewSpotInTheme {
        MenuListScreen()
    }
}