// File: app/src/main/java/com.example.brewspotin/data/model/BookingHistoryItem.kt
package com.example.brewspotin.data.model

data class BookingHistoryItem(
    val reservId: String,               // <-- reservId (dari AdminHistoryRecord)
    val adminHistoryRecordId: String,   // ID dokumen dari AdminHistoryRecord
    val dateIn: String,                 // <-- dateIn (dari AdminHistoryRecord)
    val timeIn: String,                 // <-- timeIn (dari AdminHistoryRecord)
    val totalPayment: String,           // Akan diformat "Rp X.XXX" dari priceIn
    val nameIn: String,                 // <-- nameIn (dari AdminHistoryRecord)
    val guestIn: Int,                   // <-- guestIn (dari AdminHistoryRecord, konversi Long ke Int)
    val tableIn: String                 // <-- tableIn (dari AdminHistoryRecord)
)