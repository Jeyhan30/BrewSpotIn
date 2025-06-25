// File: app/src/main/java/com/example.brewspotin/presentation/viewmodel/ReservationAndHistoryProcessorViewModel.kt
package com.example.brewspotin.ui.screen.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewspotin.data.model.ReservationRecord
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import com.google.firebase.firestore.DocumentChange
import java.util.Date
import java.util.concurrent.TimeUnit

class ReservationAndHistoryProcessorViewModel(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ViewModel() {

    private val TAG = "ResHistProcessorVM"
    private val ADMIN_CAFE_NAME = "Jokopi"
    private val AUTO_RELEASE_BUFFER_MINUTES = 15L // Menggunakan Long untuk TimeUnit

    // Flow untuk menyimpan ID meja yang sedang TAKEN oleh reservasi aktif
    private val _autoTakenTableIds = MutableStateFlow<Set<String>>(emptySet())
    val autoTakenTableIds: StateFlow<Set<String>> = _autoTakenTableIds

    init {
        Log.d(TAG, "ReservationAndHistoryProcessorViewModel initialized. Starting to listen for active reservations.")
        listenForActiveReservations()
    }

    private fun listenForActiveReservations() {
        viewModelScope.launch {
            callbackFlow<Unit> {
                val registration = firestore.collection("reservations")
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.e(TAG, "Listen failed on reservations.", e)
                            close(e)
                            return@addSnapshotListener
                        }

                        if (snapshot != null) {
                            val currentActiveTables = mutableSetOf<String>()
                            for (doc in snapshot.documents) {
                                val reservation = doc.toObject(ReservationRecord::class.java)
                                if (reservation != null && reservation.cafeName == ADMIN_CAFE_NAME) {
                                    val reservationEndTimeMillis = reservation.timestamp?.time?.plus(TimeUnit.MINUTES.toMillis(AUTO_RELEASE_BUFFER_MINUTES)) ?: 0L
                                    val currentTimeMillis = System.currentTimeMillis()

                                    if (currentTimeMillis <= reservationEndTimeMillis) {
                                        currentActiveTables.addAll(reservation.selectedTables)
                                    }
                                }
                            }
                            _autoTakenTableIds.value = currentActiveTables
                            Log.d(TAG, "Active reservation tables updated: $currentActiveTables")
                        }
                        trySend(Unit)
                    }
                awaitClose {
                    registration.remove()
                    Log.d(TAG, "Reservations listener removed.")
                }
            }.collect { }
        }
    }
}
