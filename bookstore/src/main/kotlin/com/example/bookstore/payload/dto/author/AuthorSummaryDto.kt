package com.example.bookstore.payload.dto.author

data class AuthorSummaryDto(
    val id: Long,
    val name: String? = null,
    val image: String? = null
)