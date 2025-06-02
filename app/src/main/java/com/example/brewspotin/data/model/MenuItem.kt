package com.example.brewspotin.data.model

import com.google.firebase.firestore.DocumentId

data class MenuItem(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val deskripsi: String = "",
    val harga: Long = 0,
    val gambar: String = ""
)