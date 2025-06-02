// File: MainActivity.kt
package com.example.brewspotin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.brewspotin.ui.screen.history.HistoryListScreen
import com.example.brewspotin.ui.screen.menu.MenuListScreen
import com.example.brewspotin.ui.theme.BrewSpotInTheme
import com.example.brewspotin.ui.screen.dashboard.AdminDashboardScreen
import com.example.brewspotin.ui.screen.history.AdminHistoryListViewModel
import com.example.brewspotin.ui.screen.history.ReservationAndHistoryProcessorViewModel
import com.example.brewspotin.ui.screen.menu.AddMenuScreen
import com.example.brewspotin.ui.screen.menu.MenuViewModel
import com.example.brewspotin.ui.screen.reservation.SelectTableScreen
import com.example.brewspotin.ui.screen.reservation.TableViewModel

val DarkBrown = Color(0xFF6B4226)
val MediumGrayBorder = Color(0xFFBDBDBD)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BrewSpotInTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val menuViewModel: MenuViewModel = viewModel()
                    val adminHistoryListViewModel: AdminHistoryListViewModel = viewModel() // <-- Inisialisasi ViewModel ini
                    val tableViewModel: TableViewModel = viewModel()
                    val reservationAndHistoryProcessorViewModel: ReservationAndHistoryProcessorViewModel = viewModel() // <-- Inisialisasi ViewModel ini

                    NavHost(
                        navController = navController,
                        startDestination = "admin_dashboard_screen"
                    ) {
                        composable("select_table_screen") {
                            SelectTableScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                onNavigateNext = { selectedTableId ->
                                    navController.popBackStack("admin_dashboard_screen", inclusive = false)
                                },
                                tableViewModel = tableViewModel, // Teruskan TableViewModel
                                reservationProcessorViewModel = reservationAndHistoryProcessorViewModel // Teruskan ProcessorViewModel
                            )
                        }

                        composable("add_menu_screen") {
                            AddMenuScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                viewModel = menuViewModel
                            )
                        }

                        composable("history_list_screen") {
                            HistoryListScreen( // Nama Composable tetap ini
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                viewModel = adminHistoryListViewModel // <-- Teruskan ViewModel yang benar
                            )
                        }

                        composable("menu_list_screen") {
                            MenuListScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                onNavigateToAddMenu = {
                                    navController.navigate("add_menu_screen")
                                },
                                viewModel = menuViewModel
                            )
                        }

                        composable("admin_dashboard_screen") {
                            AdminDashboardScreen(
                                onNavigateToHistory = { navController.navigate("history_list_screen") },
                                onNavigateToAddMenu = { navController.navigate("menu_list_screen") },
                                onNavigateToUpdateTable = { navController.navigate("select_table_screen") }
                            )
                            // Opsional: Tampilkan pesan proses dari ReservationAndHistoryProcessorViewModel di dashboard
                            // val processingMessage by reservationAndHistoryProcessorViewModel.processingMessage.collectAsState()
                            // processingMessage?.let { message ->
                            //     Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
                            //     reservationAndHistoryProcessorViewModel.clearProcessingMessage()
                            // }
                        }
                    }
                }
            }
        }
    }
}