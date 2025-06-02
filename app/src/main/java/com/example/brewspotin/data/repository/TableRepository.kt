// File: app/src/main/java/com/example.brewspotin/data/repository/TableRepository.kt
package com.example.brewspotin.data.repository

import android.util.Log
import com.example.brewspotin.data.model.TableStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class TableRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val TAG = "TableRepository"
    private val CAFE_ID = "Jokopi" // Sesuaikan dengan ID cafe admin

    fun getAllTableStatuses(): Flow<List<TableStatus>> = flow {
        try {
            val snapshot = firestore.collection("Cafe")
                .document(CAFE_ID)
                .collection("Table")
                .get()
                .await()

            val tableStatuses = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(TableStatus::class.java)
                } catch (e: Exception) {
                    Log.e(TAG, "Error converting table document ${doc.id}: ${e.message}", e)
                    null
                }
            }
            emit(tableStatuses)
            Log.d(TAG, "Successfully loaded ${tableStatuses.size} table statuses.")
        } catch (e: FirebaseFirestoreException) {
            Log.e(TAG, "Firebase Firestore error loading table statuses: ${e.message}", e)
            emit(emptyList())
        } catch (e: Exception) {
            Log.e(TAG, "Generic error loading table statuses: ${e.message}", e)
            emit(emptyList())
        }
    }

    suspend fun updateTableStatus(tableStatus: TableStatus): Boolean {
        return try {
            firestore.collection("Cafe")
                .document(CAFE_ID)
                .collection("Table")
                .document(tableStatus.id)
                .set(tableStatus) // Update seluruh dokumen meja
                .await()
            Log.d(TAG, "Table ${tableStatus.id} updated to status ${tableStatus.status}.")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update table status for ${tableStatus.id}: ${e.message}", e)
            false
        }
    }
}