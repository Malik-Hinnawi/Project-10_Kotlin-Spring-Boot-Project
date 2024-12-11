package com.example.bookstore.services.impl

import com.example.bookstore.entities.Book
import com.example.bookstore.payload.extensions.toBookEntity
import com.example.bookstore.payload.summary.BookSummary
import com.example.bookstore.repositories.AuthorRepository
import com.example.bookstore.repositories.BookRepository
import com.example.bookstore.services.BookService
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


@Service
class BookServiceImpl(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository
) : BookService {

    @Transactional
    override fun createUpdate(isbn: String, bookSummary: BookSummary): Pair<Book, Boolean> {
        val normalizedBook = bookSummary.copy(isbn = isbn)
        val isExists = bookRepository.existsById(isbn)

        val author = authorRepository.findByIdOrNull(normalizedBook.author.id)
        checkNotNull(author)

        val savedBook = bookRepository.save(normalizedBook.toBookEntity(author))
        return Pair(savedBook, !isExists)
    }
}