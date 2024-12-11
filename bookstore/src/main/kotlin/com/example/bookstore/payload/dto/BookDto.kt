package com.example.bookstore.payload.dto

data class BookDto(
    val isbn: String,
    val title: String,
    val description: String,
    val image: String,
    val author: AuthorDto
)