package com.example.bookstore.controllers

import com.example.bookstore.exceptions.InvalidAuthorException
import com.example.bookstore.payload.dto.book.BookSummaryDto
import com.example.bookstore.payload.extensions.toBookSummary
import com.example.bookstore.payload.extensions.toBookSummaryDto
import com.example.bookstore.services.BookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/books")
class BookController(
    private val bookService: BookService
) {

    @PutMapping("/{isbn}")
    fun createFullUpdateBook(
        @PathVariable("isbn") isbn : String,
        @RequestBody book: BookSummaryDto
    ) : ResponseEntity<BookSummaryDto>{
        return try {
            val (savedBook, isCreated) =bookService.createUpdate(isbn, book.toBookSummary())
            val responseCode = if(isCreated) HttpStatus.CREATED else HttpStatus.OK
            ResponseEntity(savedBook.toBookSummaryDto(), responseCode)
        } catch (e: InvalidAuthorException){
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: IllegalStateException){
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }
}