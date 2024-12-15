package com.example.bookstore.controllers

import com.example.bookstore.exceptions.InvalidAuthorException
import com.example.bookstore.payload.dto.book.BookSummaryDto
import com.example.bookstore.payload.dto.book.BookUpdateRequestDto
import com.example.bookstore.payload.extensions.toBookSummary
import com.example.bookstore.payload.extensions.toBookSummaryDto
import com.example.bookstore.payload.extensions.toBookUpdateRequest
import com.example.bookstore.services.BookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
         try {
            val (savedBook, isCreated) =bookService.createUpdate(isbn, book.toBookSummary())
            val responseCode = if(isCreated) HttpStatus.CREATED else HttpStatus.OK
            return ResponseEntity(savedBook.toBookSummaryDto(), responseCode)
        } catch (e: InvalidAuthorException){
            return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: IllegalStateException){
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping
    fun listAllBooks(@RequestParam("author") authorId: Long? ): List<BookSummaryDto> {
        val listOfBooks = bookService.list(authorId).map { it.toBookSummaryDto() }
        return listOfBooks
    }

    @GetMapping("/{isbn}")
    fun readOneBook(@PathVariable("isbn") isbn : String) : ResponseEntity<BookSummaryDto>{
        return bookService.get(isbn)?.let {
            ResponseEntity(it.toBookSummaryDto(), HttpStatus.OK)
        }
        ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PatchMapping("/{isbn}")
    fun partialUpdateBook(
        @PathVariable("isbn") isbn : String,
        @RequestBody request: BookUpdateRequestDto
    ) : ResponseEntity<BookSummaryDto>{
        try {
            val updatedBook = bookService.partialUpdate(isbn, request.toBookUpdateRequest())
            return ResponseEntity.ok(updatedBook.toBookSummaryDto())
        } catch (e: IllegalStateException){
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{isbn}")
    fun deleteBook(@PathVariable("isbn") isbn : String) : ResponseEntity<Unit>{
        bookService.delete(isbn)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}