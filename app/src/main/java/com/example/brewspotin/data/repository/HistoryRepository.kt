// File: app/src/main/java/com.example.brewspotin/data/repository/HistoryRepository.kt
package com.example.brewspotin.data.repository

import android.util.Log
import com.example.brewspotin.data.model.ReservationRecord
import com.example.brewspotin.data.model.UserHistoryRecord
import com.example.brewspotin.data.model.AdminHistoryRecord
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class HistoryRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val TAG = "HistoryRepository"

    // Fungsi untuk mendapatkan satu ReservationRecord berdasarkan ID
    suspend fun getReservationRecordById(reservationId: String): ReservationRecord? {
        return try {
            val document = firestore.collection("reservations")
                .document(reservationId)
                .get()
                .await()
            document.toObject(ReservationRecord::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting reservation record by ID $reservationId: ${e.message}", e)
            null
        }
    }

    // Fungsi untuk mendapatkan semua UserHistoryRecord dari koleksi 'history' (User View)
    fun getAllUserHistoryRecords(): Flow<List<UserHistoryRecord>> = flow {
        try {
            val snapshot = firestore.collection("history")
                .get()
                .await()

            val userHistory = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(UserHistoryRecord::class.java)
                } catch (e: Exception) {
                    Log.e(TAG, "Error converting user history document ${doc.id}: ${e.message}", e)
                    null
                }
            }
            emit(userHistory)
            Log.d(TAG, "Successfully loaded ${userHistory.size} user history records.")
        } catch (e: FirebaseFirestoreException) {
            Log.e(TAG, "Firebase Firestore error loading user history: ${e.message}", e)
            emit(emptyList())
        } catch (e: Exception) {
            Log.e(TAG, "Generic error loading user history: ${e.message}", e)
            emit(emptyList())
        }
    }

    // Fungsi untuk mendapatkan semua AdminHistoryRecord dari koleksi 'adminhistory' (Admin View)
    fun getAllAdminHistoryRecords(): Flow<List<AdminHistoryRecord>> = flow {
        try {
            val snapshot = firestore.collection("adminhistory")
                .get()
                .await()

            val adminHistory = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(AdminHistoryRecord::class.java)
                } catch (e: Exception) {
                    Log.e(TAG, "Error converting admin history document ${doc.id}: ${e.message}", e)
                    null
                }
            }
            emit(adminHistory)
            Log.d(TAG, "Successfully loaded ${adminHistory.size} admin history records for display.")
        } catch (e: FirebaseFirestoreException) {
            Log.e(TAG, "Firebase Firestore error loading admin history for display: ${e.message}", e)
            emit(emptyList())
        } catch (e: Exception) {
            Log.e(TAG, "Generic error loading admin history for display: ${e.message}", e)
            emit(emptyList())
        }
    }

    // Fungsi untuk menambah AdminHistoryRecord ke koleksi 'adminhistory'
    suspend fun addAdminHistoryRecord(adminHistory: AdminHistoryRecord): Boolean {
        return try {
            firestore.collection("adminhistory")
                .document(adminHistory.id) // Menggunakan ID yang diberikan (dari ID reservasi)
                .set(adminHistory)
                .await()
            Log.d(TAG, "AdminHistory record ${adminHistory.id} added/updated successfully.")
            true
        } catch (e: FirebaseFirestoreException) {
            Log.e(TAG, "Firebase Firestore error adding admin history: ${e.message}", e)
            false
        } catch (e: Exception) {
            Log.e(TAG, "Generic error adding admin history: ${e.message}", e)
            false
        }
    }
}
