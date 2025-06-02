package com.example.brewspotin.ui.screen.reservation

// File: app/src/main/java/com/example.brewspotin/presentation/viewmodel/TableViewModel.kt
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewspotin.data.model.TableOrigin
import com.example.brewspotin.data.model.TableStatus
import com.example.brewspotin.data.repository.TableRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit

class TableViewModel(
    private val tableRepository: TableRepository = TableRepository()
) : ViewModel() {

    private val TAG = "TableViewModel"
    private val AUTO_RELEASE_DURATION_MINUTES = 15L // Durasi otomatis bebas

    private val _tableStatuses = MutableStateFlow<List<TableStatus>>(emptyList())
    val tableStatuses: StateFlow<List<TableStatus>> = _tableStatuses

    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage: StateFlow<String?> = _uiMessage

    init {
        // Memulai pemantauan status meja dari database
        viewModelScope.launch {
            tableRepository.getAllTableStatuses()
                .collect { dbTableStatuses ->
                    // Lakukan pengecekan dan update otomatis (jika ada)
                    val updatedStatuses = dbTableStatuses.map { table ->
                        checkAndAutoFreeTable(table)
                    }
                    _tableStatuses.value = updatedStatuses
                    Log.d(TAG, "Table statuses from DB loaded/updated. Current: ${_tableStatuses.value.map { it.id + ": " + it.status }}")
                }
        }
    }

    // Fungsi untuk mengupdate status meja secara manual dari UI admin
    fun updateTableStatus(tableId: String, newStatus: String) {
        viewModelScope.launch {
            val currentTable = _tableStatuses.value.find { it.id == tableId }
            val newTableStatus: TableStatus = when (newStatus) {
                "TAKEN" -> {
                    // Jika admin menyetel ke TAKEN, selalu ADMIN_MANUAL
                    currentTable?.copy(
                        status = "TAKEN",
                        statusOrigin = TableOrigin.ADMIN_MANUAL.name,
                        takenTimestamp = Date(),
                        takenByReservationId = null
                    ) ?: TableStatus( // <-- Jika meja belum ada di DB, buat yang baru
                        id = tableId,
                        status = "TAKEN",
                        statusOrigin = TableOrigin.ADMIN_MANUAL.name,
                        takenTimestamp = Date()
                    )
                }
                "AVAILABLE" -> {
                    // Jika admin menyetel ke AVAILABLE, selalu ADMIN_MANUAL
                    currentTable?.copy(
                        status = "AVAILABLE",
                        statusOrigin = TableOrigin.ADMIN_MANUAL.name,
                        takenTimestamp = null,
                        takenByReservationId = null
                    ) ?: TableStatus( // <-- Jika meja belum ada di DB, buat yang baru
                        id = tableId,
                        status = "AVAILABLE",
                        statusOrigin = TableOrigin.ADMIN_MANUAL.name
                    )
                }
                else -> throw IllegalArgumentException("Invalid status: $newStatus")
            }

            val success = tableRepository.updateTableStatus(newTableStatus)
            if (success) {
                _uiMessage.value = "Status meja $tableId berhasil diubah menjadi $newStatus."
                // loadTableStatuses() // Otomatis terpicu oleh listener di init {}
            } else {
                _uiMessage.value = "Gagal mengubah status meja $tableId."
            }
        }
    }

    // Memeriksa dan membebaskan meja otomatis jika statusnya AUTO_RESERVATION dan sudah lewat waktu
    private suspend fun checkAndAutoFreeTable(table: TableStatus): TableStatus {
        return if (table.status == "TAKEN" && table.statusOrigin == TableOrigin.AUTO_RESERVATION.name) {
            val takenMillis = table.takenTimestamp?.time ?: 0L
            val currentTimeMillis = System.currentTimeMillis()
            val fifteenMinutesAgo = currentTimeMillis - TimeUnit.MINUTES.toMillis(AUTO_RELEASE_DURATION_MINUTES)

            if (takenMillis > 0L && takenMillis < fifteenMinutesAgo) {
                // Meja sudah lewat waktu 15 menit, otomatis bebaskan
                Log.d(TAG, "Table ${table.id} (AUTO_RESERVATION) is overdue. Auto-freeing it.")
                val newTableStatus = table.copy(
                    status = "AVAILABLE",
                    statusOrigin = TableOrigin.ADMIN_MANUAL.name, // Ubah origin saat dibebaskan
                    takenTimestamp = null,
                    takenByReservationId = null
                )
                tableRepository.updateTableStatus(newTableStatus) // Update ke DB
                newTableStatus // Kembalikan status baru
            } else {
                table // Belum waktunya dibebaskan
            }
        } else {
            table // Bukan AUTO_RESERVATION atau sudah AVAILABLE
        }
    }

    fun clearUiMessage() {
        _uiMessage.value = null
    }
}