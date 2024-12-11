package com.example.bookstore.payload.dto.book

import com.example.bookstore.payload.dto.author.AuthorDto

data class BookDto(
    val isbn: String,
    val title: String,
    val description: String,
    val image: String,
    val author: AuthorDto
)