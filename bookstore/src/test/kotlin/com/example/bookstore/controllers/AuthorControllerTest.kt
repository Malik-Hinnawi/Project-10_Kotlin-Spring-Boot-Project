package com.example.bookstore.controllers

import com.example.bookstore.entities.Author
import com.example.bookstore.services.AuthorService
import com.example.bookstore.testAuthorDtoA
import com.example.bookstore.testAuthorEntityA
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

private const val AUTHOR_BASE_URL = "/v1/authors"

@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    @MockkBean val authorService: AuthorService
    ) {

    val objectMapper = ObjectMapper()

    @BeforeEach
    fun beforeEach(){
        every {
            authorService.create(any())
        } answers {
            firstArg()
        }
    }

    @Test
    fun `create author and save author`(){
        mockMvc.post(AUTHOR_BASE_URL){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
               testAuthorDtoA()
            )
        }

        val expected = Author(
            id= null,
            name = "John Doe",
            age = 10,
            image = "test.jpg",
            description = "bla"
        )
        verify {
            authorService.create(expected)
        }
    }

    @Test
    fun `create Author returns a HTTP 201 status on successful create` (){
       mockMvc.post(AUTHOR_BASE_URL){
           contentType = MediaType.APPLICATION_JSON
           accept = MediaType.APPLICATION_JSON
           content = objectMapper.writeValueAsString(
              testAuthorDtoA()
           )
       }.andExpect { status { isCreated() } }
    }

    @Test
    fun `test that list returns an empty list and HTTp 20 when no authors in the db`(){
        every {
            authorService.list()
        } answers {
            emptyList()
        }

        mockMvc.get(AUTHOR_BASE_URL){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { json("[]") }
        }
    }

    @Test
    fun `test that list returns authors and HTTP 200 when authors in db`(){
        every {
            authorService.list()
        } answers {
            listOf(testAuthorEntityA(1))
        }

        mockMvc.get(AUTHOR_BASE_URL){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$[0].id", equalTo(1)) }
            content { jsonPath("$[0].name", equalTo("John Doe")) }
            content { jsonPath("$[0].age", equalTo(10)) }
            content { jsonPath("$[0].description", equalTo("bla")) }
            content { jsonPath("$[0].image", equalTo("test.jpg")) }
        }
    }

    @Test
    fun `test that HTTP 404 when author not found in db`(){

        every {
            authorService.getById(any())
        } answers {
            null
        }

        mockMvc.get("${AUTHOR_BASE_URL}/999"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `test that get returns HTTP 200 and author when author found` (){
        every {
            authorService.getById(any())
        } answers {
            testAuthorEntityA(id=999)
        }

        mockMvc.get("${AUTHOR_BASE_URL}/999"){
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.id", equalTo(999)) }
            content { jsonPath("$.name", equalTo("John Doe")) }
            content { jsonPath("$.age", equalTo(10)) }
            content { jsonPath("$.description", equalTo("bla")) }
            content { jsonPath("$.image", equalTo("test.jpg")) }
        }
    }
}