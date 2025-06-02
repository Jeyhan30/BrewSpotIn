// File: app/src/main/java/com/example.brewspotin/data/model/ReservationRecord.kt
package com.example.brewspotin.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ReservationRecord(
    @DocumentId
    val id: String = "", // ID dokumen dari koleksi 'reservations'
    val cafeId: String = "",
    val cafeName: String = "",
    val date: String = "", // Tanggal reservasi (misal "DD/MM/YYYY")
    val selectedTables: List<String> = emptyList(), // Meja yang dipilih (List/Array of String)
    val time: String = "", // Waktu reservasi (misal "HH.mm AM/PM")
    @ServerTimestamp
    val timestamp: Date? = null,
    val totalGuests: Long = 0, // Total tamu (Long)
    val userId: String = "",
    val userName: String = ""
    // Catatan: totalPrice tidak ada di ReservationRecord ini, akan diambil dari UserHistoryRecord
)