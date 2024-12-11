package com.example.bookstore.services

import com.example.bookstore.entities.Book
import com.example.bookstore.payload.summary.BookSummary


interface BookService {
    fun createUpdate(isbn: String, bookSummary: BookSummary) : Pair<Book,Boolean>

}