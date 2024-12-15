package com.example.bookstore.services

import com.example.bookstore.entities.Book
import com.example.bookstore.payload.request.BookUpdateRequest
import com.example.bookstore.payload.summary.BookSummary


interface BookService {
    fun createUpdate(isbn: String, bookSummary: BookSummary) : Pair<Book,Boolean>
    fun list(authorId: Long? = null): List<Book>
    fun get(isbn: String) : Book?
    fun partialUpdate(isbn: String, bookUpdateRequest: BookUpdateRequest) : Book
    fun delete(isbn: String)
}