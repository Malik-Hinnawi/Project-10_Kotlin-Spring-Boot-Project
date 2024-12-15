package com.example.bookstore.repositories

import com.example.bookstore.entities.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, String>{
    fun findByAuthorId(id: Long) : List<Book>
}