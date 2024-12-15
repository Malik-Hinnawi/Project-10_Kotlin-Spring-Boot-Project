package com.example.bookstore.controllers

import com.example.bookstore.*
import com.example.bookstore.exceptions.InvalidAuthorException
import com.example.bookstore.payload.dto.book.BookUpdateRequestDto
import com.example.bookstore.payload.extensions.toAuthorSummaryDto
import com.example.bookstore.payload.extensions.toBookUpdateRequest
import com.example.bookstore.payload.request.BookUpdateRequest
import com.example.bookstore.services.BookService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.result.StatusResultMatchersDsl

private const val BASE_URL = "/v1/books"

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest@Autowired constructor(
    private val mockMvc: MockMvc,
    @MockkBean val bookService: BookService
)  {
    val objectMapper = ObjectMapper()

    @Test
    fun `test that createFullUpdateBook return HTTP 201 when book is created`(){
        assertThatUserCreatedUpdated(true){isCreated()}
    }

    @Test
    fun `test that createFullUpdateBook return HTTP 200 when book is updated`(){
        assertThatUserCreatedUpdated(false){isOk()}
    }

    @Test
    fun `test that createFullUpdateBook return HTTP 500 when author id is missing`(){
        val isbn = "111-111-111111-111"

        val authorSummaryDto = testAuthorSummaryDtoA(1)
        val bookSummaryDto = testBookSummaryDtoA(isbn, authorSummaryDto)

        every {
            bookService.createUpdate(isbn, any())
        } throws InvalidAuthorException()

        mockMvc.put("$BASE_URL/${isbn}"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookSummaryDto)
        }.andExpect {
            status { isInternalServerError() }
        }
    }

    @Test
    fun `test that createFullUpdateBook return HTTP 400 when author does not exist`(){
        val isbn = "111-111-111111-111"

        val authorSummaryDto = testAuthorSummaryDtoA(1)
        val bookSummaryDto = testBookSummaryDtoA(isbn, authorSummaryDto)

        every {
            bookService.createUpdate(isbn, any())
        } throws IllegalStateException()

        mockMvc.put("$BASE_URL/${isbn}"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookSummaryDto)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `test that readManyBooks returns a list of books`(){
        every { bookService.list() } answers { listOf(testBookEntityA(BOOK_A_ISBN, testAuthorEntityA(id=1))) }
        mockMvc.get(BASE_URL){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$[0].isbn", equalTo(BOOK_A_ISBN)) }
            content { jsonPath("$[0].title", equalTo("Test Book A")) }
            content { jsonPath("$[0].description", equalTo("A test description")) }
            content { jsonPath("$[0].author.id", equalTo(1)) }
        }
    }

    @Test
    fun `test that list returns no books when they do not match the author Id`(){
        every {
            bookService.list(authorId = any())
        } answers { emptyList() }

        mockMvc.get("$BASE_URL?author=99"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { json("[]") }
        }

    }

    @Test
    fun `test that list returns book when matches author Id`(){
        every {
            bookService.list(authorId = 1L)
        } answers { listOf(
            testBookEntityA(
                isbn = BOOK_A_ISBN,
                author = testAuthorEntityA(1L)
                )
        ) }

        mockMvc.get("$BASE_URL?author=1"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$[0].isbn", equalTo(BOOK_A_ISBN)) }
            content { jsonPath("$[0].title", equalTo("Test Book A")) }
            content { jsonPath("$[0].description", equalTo("A test description")) }
            content { jsonPath("$[0].author.id", equalTo(1)) }
        }
    }

    private fun assertThatUserCreatedUpdated(isCreated: Boolean, statusCodeAssertion: StatusResultMatchersDsl.()-> Unit){
        val isbn = "111-111-111111-111"
        val author = testAuthorEntityA(1)
        val savedBook = testBookEntityA(isbn, author)
        val bookSummaryDto = testBookSummaryDtoA(isbn, author.toAuthorSummaryDto())
        every {
            bookService.createUpdate(isbn, any())
        } answers {
            Pair(savedBook, isCreated)
        }

        mockMvc.put("$BASE_URL/${isbn}"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookSummaryDto)
        }.andExpect {
            status { statusCodeAssertion() }
        }
    }


    @Test
    fun `test that readOneBook returns HTTP 404 when book not found`(){
        every {
            bookService.get(any())
        } answers {
            null
        }

        mockMvc.get("$BASE_URL/$BOOK_A_ISBN"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `test that readOneBook returns HTTP 200 and book when book is found`(){
        every {
            bookService.get(BOOK_A_ISBN)
        } answers {
            testBookEntityA(isbn = BOOK_A_ISBN, author = testAuthorEntityA(id=1L))
        }

        mockMvc.get("$BASE_URL/$BOOK_A_ISBN"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.isbn", equalTo(BOOK_A_ISBN)) }
            content { jsonPath("$.title", equalTo("Test Book A")) }
            content { jsonPath("$.description", equalTo("A test description")) }
            content { jsonPath("$.author.id", equalTo(1)) }
        }
    }

    @Test
    fun `test that book partial update returns a HTTP 400 on IllegalStateException`(){
        val bookUpdateRequestDto = BookUpdateRequestDto(
            title = "New title"
        )

        every {
            bookService.partialUpdate(BOOK_A_ISBN, bookUpdateRequestDto.toBookUpdateRequest())
        } throws IllegalStateException()

        mockMvc.patch("$BASE_URL/${BOOK_A_ISBN}"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookUpdateRequestDto)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `test that book partial update returns a HTTP 200 and book when book is found`(){
        val bookUpdateRequestDto = BookUpdateRequestDto(
            title = "New title"
        )

        val bookEntity = testBookEntityA(isbn = BOOK_A_ISBN, testAuthorEntityA(id=1L)).copy(
            title = bookUpdateRequestDto.title!!
        )

        every {
            bookService.partialUpdate(BOOK_A_ISBN, bookUpdateRequestDto.toBookUpdateRequest())
        } answers {
            bookEntity
        }

        mockMvc.patch("$BASE_URL/${BOOK_A_ISBN}"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bookUpdateRequestDto)
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.isbn", equalTo(BOOK_A_ISBN)) }
            content { jsonPath("$.title", equalTo("New title")) }
            content { jsonPath("$.description", equalTo("A test description")) }
            content { jsonPath("$.author.id", equalTo(1)) }
        }
    }

    @Test
    fun `test that deleteBook deletes a book successfully`(){
        every { bookService.delete(BOOK_A_ISBN) } answers {}

        mockMvc.delete("$BASE_URL/$BOOK_A_ISBN"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNoContent() }
        }
    }

}