package com.example.bookstore.payload.dto.book

import com.example.bookstore.payload.dto.author.AuthorSummaryDto

data class BookSummaryDto(
    val isbn: String,
    val title: String,
    val description: String,
    val image: String,
    val author: AuthorSummaryDto
)