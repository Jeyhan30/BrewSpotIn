// File: app/src/main/java/com.example.brewspotin/data/model/AdminHistoryRecord.kt
package com.example.brewspotin.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class AdminHistoryRecord(
    @DocumentId // Ini akan menjadi ID dokumen dari 'adminhistory' (misalnya ID reservasi)
    val id: String = "", // Disarankan pakai ID reservasi untuk memudahkan lookup
    val reservationId: String = "", // <-- reservId (dari history user view)
    val dateIn: String = "",          // <-- date (dari reservations user view)
    val priceIn: Long = 0,          // <-- totalPrice (dari history user view)
    val nameIn: String = "",          // <-- username (dari history user view)
    val timeIn: String = "",          // <-- time (dari reservations user view)
    val guestIn: Long = 0,          // <-- totalGuests (dari reservations user view)
    val tableIn: String = "",         // <-- selectedTables (dari reservations user view, digabung string)
    @ServerTimestamp // Timestamp kapan record adminhistory ini dibuat
    val adminTimestamp: Date? = null
    // Tambahan field untuk referensi dan debugging
    // val userHistoryId: String = "" // ID dokumen dari history user view
    // val cafeId: String = ""
    // val cafeName: String = ""
)