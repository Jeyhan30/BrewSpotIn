// File: app/src/main/java/com.example.brewspotin/data/model/UserHistoryRecord.kt
package com.example.brewspotin.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class OrderItem(
    val cafeId: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val menuItemId: String = "",
    val name: String = "",
    val priceAtOrder: Long = 0,
    val quantity: Long = 0
)

data class UserHistoryRecord(
    @DocumentId
    val id: String = "",
    val appFeeAmount: Long = 0,
    val cafeId: String = "",
    val cafeName: String = "",
    val downPaymentAmount: Long = 0,
    val items: List<OrderItem> = emptyList(), // List of OrderItem
    val reservationId: String = "",
    @ServerTimestamp
    val timestamp: Date? = null,
    val totalMenuOrderPrice: Long = 0, // Total harga pesanan menu
    val totalPrice: Long = 0, // Total pembayaran keseluruhan
    val userEmail: String = "",
    val userId: String = "",
    val userPhone: String = "",
    val username: String = ""
)
