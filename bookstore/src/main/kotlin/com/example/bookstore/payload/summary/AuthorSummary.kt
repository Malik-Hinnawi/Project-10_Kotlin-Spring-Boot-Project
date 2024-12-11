package com.example.bookstore.payload.summary

data class AuthorSummary(
    val id: Long,
    val name: String? = null,
    val image: String? = null
)