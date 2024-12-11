package com.example.bookstore.services

import com.example.bookstore.entities.Author
import com.example.bookstore.payload.request.AuthorUpdateRequest

interface AuthorService {
    fun create(author: Author): Author
    fun list(): List<Author>
    fun getById(id: Long): Author?
    fun fullUpdate(id: Long, author: Author) : Author
    fun partialUpdate(id: Long, authorUpdate: AuthorUpdateRequest) : Author
    fun delete(id: Long)
}