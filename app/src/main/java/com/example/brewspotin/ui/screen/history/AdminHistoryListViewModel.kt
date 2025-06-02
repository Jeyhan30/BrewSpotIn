// File: app/src/main/java/com.example.brewspotin/presentation/viewmodel/AdminHistoryListViewModel.kt
package com.example.brewspotin.ui.screen.history
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewspotin.data.model.BookingHistoryItem
import com.example.brewspotin.data.model.AdminHistoryRecord // Import AdminHistoryRecord
import com.example.brewspotin.data.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AdminHistoryListViewModel(
    private val repository: HistoryRepository = HistoryRepository()
) : ViewModel() {

    private val _bookingHistory = MutableStateFlow<List<BookingHistoryItem>>(emptyList())
    val bookingHistory: StateFlow<List<BookingHistoryItem>> = _bookingHistory

    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage

    init {
        loadAdminBookingHistory()
    }

    // Fungsi untuk memuat dan memetakan data riwayat booking dari adminhistory
    fun loadAdminBookingHistory() {
        viewModelScope.launch {
            repository.getAllAdminHistoryRecords() // <-- Mengambil dari adminhistory
                .collectLatest { adminHistories -> // Menerima daftar AdminHistoryRecord
                    val mappedList = adminHistories.map { adminHistory ->
                        // Mapping AdminHistoryRecord ke BookingHistoryItem
                        BookingHistoryItem(
                            reservId = adminHistory.reservationId, // <-- reservId (dari AdminHistoryRecord)
                            adminHistoryRecordId = adminHistory.id,
                            dateIn = adminHistory.dateIn,
                            timeIn = adminHistory.timeIn,
                            totalPayment = "Rp ${adminHistory.priceIn}", // Dari priceIn
                            nameIn = adminHistory.nameIn,
                            guestIn = adminHistory.guestIn.toInt(), // Konversi Long ke Int
                            tableIn = adminHistory.tableIn // Sudah string
                        )
                    }
                    _bookingHistory.value = mappedList.sortedByDescending { it.dateIn } // Optional: Sorting
                    if (mappedList.isEmpty()) {
                        _uiMessage.value = "Tidak ada riwayat booking admin."
                    } else {
                        _uiMessage.value = null
                    }
                    Log.d("AdminHistoryViewModel", "Mapped admin booking history loaded: ${mappedList.size} items")
                }
        }
    }

    fun clearUiMessage() {
        _uiMessage.value = null
    }
}