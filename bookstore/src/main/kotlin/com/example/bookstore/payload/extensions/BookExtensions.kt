package com.example.bookstore.payload.extensions

import com.example.bookstore.entities.Author
import com.example.bookstore.entities.Book
import com.example.bookstore.payload.dto.book.BookSummaryDto
import com.example.bookstore.payload.dto.book.BookUpdateRequestDto
import com.example.bookstore.payload.request.BookUpdateRequest
import com.example.bookstore.payload.summary.BookSummary


fun BookSummary.toBookEntity(author: Author) =  Book(
    isbn = this.isbn,
    image = this.image,
    author = author,
    description = this.description,
    title = this.title
)

fun BookSummaryDto.toBookSummary() = BookSummary(
    isbn = this.isbn,
    image = this.image,
    description = this.description,
    author = this.author.toAuthorSummary(),
    title = this.title
)

fun Book.toBookSummaryDto() = BookSummaryDto(
    isbn = this.isbn,
    image = this.image,
    title = this.title,
    description = this.description,
    author = this.author.toAuthorSummaryDto()
)

fun BookUpdateRequestDto.toBookUpdateRequest() = BookUpdateRequest(
    image = this.image,
    title = this.title,
    description = this.description
)