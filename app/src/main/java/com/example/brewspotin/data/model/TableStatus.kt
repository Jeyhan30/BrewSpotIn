// File: app/src/main/java/com/example.brewspotin/data/model/TableStatus.kt
package com.example.brewspotin.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

enum class TableOrigin {
    AUTO_RESERVATION, // Diambil oleh reservasi (akan otomatis bebas setelah 15 menit)
    ADMIN_MANUAL      // Disetel manual oleh admin (tidak otomatis bebas)
}

data class TableStatus(
    @DocumentId
    val id: String = "", // ID meja (e.g., "T1", "T2")
    val status: String = "AVAILABLE", // "AVAILABLE", "TAKEN"
    val statusOrigin: String = TableOrigin.ADMIN_MANUAL.name, // "AUTO_RESERVATION" atau "ADMIN_MANUAL"
    val takenByReservationId: String? = null, // ID reservasi jika AUTO_RESERVATION
    @ServerTimestamp
    val takenTimestamp: Date? = null, // Waktu meja menjadi TAKEN
    val cafeId: String = "Jokopi" // Bisa ditambahkan jika ada banyak kafe
)