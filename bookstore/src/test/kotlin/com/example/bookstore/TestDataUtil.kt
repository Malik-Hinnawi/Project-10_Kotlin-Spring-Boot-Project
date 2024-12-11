package com.example.bookstore

import com.example.bookstore.entities.Author
import com.example.bookstore.payload.dto.AuthorDto

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
