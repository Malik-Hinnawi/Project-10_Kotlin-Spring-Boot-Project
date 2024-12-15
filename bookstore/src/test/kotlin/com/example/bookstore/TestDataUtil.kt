package com.example.bookstore

import com.example.bookstore.entities.Author
import com.example.bookstore.entities.Book
import com.example.bookstore.payload.dto.author.AuthorDto
import com.example.bookstore.payload.dto.author.AuthorSummaryDto
import com.example.bookstore.payload.dto.book.BookSummaryDto
import com.example.bookstore.payload.extensions.toAuthorSummaryDto
import com.example.bookstore.payload.request.AuthorUpdateRequest
import com.example.bookstore.payload.summary.AuthorSummary
import com.example.bookstore.payload.summary.BookSummary

const val BOOK_A_ISBN = "111-111-111111-111"

fun testAuthorDtoA(id: Long? = null) = AuthorDto(
        id=id,
        name = "John Doe",
        age = 10,
        description = "bla",
        image = "test.jpg"
)

fun testAuthorEntityA(id: Long? = null) = Author(
    id=id,
    name = "John Doe",
    age = 10,
    description = "bla",
    image = "test.jpg"
)


fun testAuthorEntityB(id: Long? = null) = Author(
    id=id,
    name = "Don",
    age = 10,
    description = "bla",
    image = "test.jpg"
)


fun testAuthorEntityC(id: Long? = null) = Author(
    id=id,
    name = "John Doe",
    age = 15,
    description = "bla",
    image = "test.jpg"
)

fun testAuthorSummaryDtoA(id: Long) = AuthorSummaryDto(
    id = id,
    name = "John Doe",
    image = "test.jpg"
)

fun testAuthorSummary(id: Long) = AuthorSummary(
    id = id,
    name = "John Doe",
    image = "test.jpg"
)

fun testAuthorUpdateRequestA(id: Long? = null) = AuthorUpdateRequest(
    name = "John Doe",
    age = 15,
    description = "bla",
    image = "test.jpg"
)

fun testBookEntityA(isbn: String, author: Author) = Book(
    isbn = isbn,
    title = "Test Book A",
    description = "A test description",
    image = "test.jpg",
    author = author
)

fun testBookSummaryDtoA(isbn: String, author: AuthorSummaryDto) = BookSummaryDto(
    isbn = isbn,
    title = "Test Book A",
    description = "A test description",
    image = "test.jpg",
    author = author
)

fun testBookSummaryA(isbn: String, author: AuthorSummary) = BookSummary(
    isbn = isbn,
    title = "Test Book A",
    description = "A test description",
    image = "test.jpg",
    author = author
)


