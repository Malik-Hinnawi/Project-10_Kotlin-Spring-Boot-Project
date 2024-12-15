package com.example.bookstore.payload.dto.book

data class BookUpdateRequestDto(
    val title: String? = null,
    val description: String? = null,
    val image: String? = null
)