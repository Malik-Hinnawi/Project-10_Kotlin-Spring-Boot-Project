package com.example.bookstore.payload.request

data class BookUpdateRequest(
    val title: String? = null,
    val description: String? = null,
    val image: String? = null
)