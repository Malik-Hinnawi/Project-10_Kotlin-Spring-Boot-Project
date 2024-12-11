package com.example.bookstore.payload.summary

data class BookSummary(
    val isbn: String,
    val title: String,
    val description: String,
    val image: String,
    val author: AuthorSummary
)