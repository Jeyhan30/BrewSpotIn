//// File: app/src/main/java/com.example.brewspotin/presentation/viewmodel/ReservationProcessorViewModel.kt
//package com.example.brewspotin.ui.screen.reservation
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.brewspotin.data.model.ReservationRecord
//import com.example.brewspotin.data.model.AdminHistoryRecord
//import com.example.brewspotin.data.repository.HistoryRepository // Untuk addAdminHistoryRecord
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.callbackFlow
//import com.google.firebase.firestore.DocumentChange // Import ini
//import java.util.Date
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.channels.awaitClose
//
//
//class ReservationProcessorViewModel(
//    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
//    private val historyRepository: HistoryRepository = HistoryRepository() // Repositori untuk menulis ke adminhistory
//) : ViewModel() {
//
//    private val TAG = "ResProcessorVM"
//    private val ADMIN_CAFE_NAME = "Jokopi" // Nama cafe yang ingin difilter
//    private val _processingMessage = MutableStateFlow<String?>(null)
//    val processingMessage: StateFlow<String?> = _processingMessage
//
//    init {
//        Log.d(TAG, "ReservationProcessorViewModel initialized. Starting to listen for reservations.")
//        listenForNewReservations()
//    }
//
//    private fun listenForNewReservations() {
//        viewModelScope.launch {
//            callbackFlow {
//                val registration = firestore.collection("reservations")
//                    // Untuk efisiensi, bisa tambahkan filter atau query tertentu,
//                    // misalnya hanya reservasi yang baru ditambahkan setelah waktu tertentu.
//                    .addSnapshotListener { snapshot, e ->
//                        if (e != null) {
//                            Log.e(TAG, "Listen failed.", e)
//                            _processingMessage.value = "Error memantau reservasi: ${e.message}"
//                            close(e)
//                            return@addSnapshotListener
//                        }
//
//                        if (snapshot != null) {
//                            for (dc in snapshot.documentChanges) {
//                                // Hanya tertarik pada dokumen yang BARU DITAMBAHKAN
//                                if (dc.type == DocumentChange.Type.ADDED) {
//                                    val reservation = dc.document.toObject(ReservationRecord::class.java)
//                                    Log.d(TAG, "Detected new reservation: ${reservation.id} for ${reservation.cafeName}")
//
//                                    // Hanya proses jika reservasi untuk cafe admin
//                                    if (reservation.cafeName == ADMIN_CAFE_NAME) {
//                                        viewModelScope.launch { // Launch coroutine baru untuk proses ini
//                                            _processingMessage.value = "Memproses reservasi baru untuk ${ADMIN_CAFE_NAME}..."
//                                            val success = processAndSaveAdminHistoryRecord(reservation)
//                                            if (success) {
//                                                _processingMessage.value = "Reservasi ${reservation.id} berhasil diproses ke Admin History."
//                                            } else {
//                                                _processingMessage.value = "Gagal memproses reservasi ${reservation.id} ke Admin History."
//                                            }
//                                        }
//                                    } else {
//                                        Log.d(TAG, "Skipping reservation for ${reservation.cafeName}. Not for ${ADMIN_CAFE_NAME}.")
//                                    }
//                                }
//                            }
//                        }
//                    }
//                awaitClose {
//                    registration.remove() // Hapus listener saat ViewModel dibersihkan
//                    Log.d(TAG, "Reservation listener removed.")
//                }
//            }.collect {
//                // Collect block ini tidak akan memancarkan nilai,
//                // karena kita hanya menggunakan callbackFlow untuk addSnapshotListener
//            }
//        }
//    }
//
//    private suspend fun processAndSaveAdminHistoryRecord(reservation: ReservationRecord): Boolean {
//        // Buat AdminHistoryRecord dari ReservationRecord yang difilter
//        val adminHistoryRecord = AdminHistoryRecord(
//            id = reservation.id, // Gunakan ID dokumen reservasi sebagai ID dokumen adminhistory
//            reservationId = reservation.id,
//            cafeId = reservation.cafeId,
//            cafeName = reservation.cafeName,
//            date = reservation.date,
//            time = reservation.time,
//            totalGuests = reservation.totalGuests,
//            selectedTables = reservation.selectedTables,
//            userName = reservation.userName,
//            adminTimestamp = Date() // Set timestamp saat ini
//        )
//
//        // Panggil repositori untuk menyimpan ke Firestore
//        return historyRepository.addAdminHistoryRecord(adminHistoryRecord)
//    }
//
//    // Fungsi untuk menghilangkan pesan proses (opsional)
//    fun clearProcessingMessage() {
//        _processingMessage.value = null
//    }
//}