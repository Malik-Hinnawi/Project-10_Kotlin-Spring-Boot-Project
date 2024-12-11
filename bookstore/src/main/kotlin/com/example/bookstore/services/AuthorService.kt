package com.example.bookstore.services

import com.example.bookstore.entities.Author

interface AuthorService {
    fun create(author: Author): Author
    fun list(): List<Author>
    fun getById(id: Long): Author?
    fun fullUpdate(id: Long, author: Author) : Author
}