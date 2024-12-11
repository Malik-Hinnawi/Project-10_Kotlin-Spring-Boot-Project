package com.example.bookstore

import com.example.bookstore.entities.Author
import com.example.bookstore.payload.dto.author.AuthorDto
import com.example.bookstore.payload.request.AuthorUpdateRequest

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

fun testAuthorUpdateRequestA(id: Long? = null) = AuthorUpdateRequest(
    name = "John Doe",
    age = 15,
    description = "bla",
    image = "test.jpg"
)


