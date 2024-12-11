package com.example.bookstore.payload.extensions

import com.example.bookstore.entities.Author
import com.example.bookstore.payload.dto.AuthorDto

fun Author.toAuthorDto()= AuthorDto(
        id = this.id,
        name = this.name,
        age = this.age,
        description = this.description,
        image = this.image
)

fun AuthorDto.toAuthor()= Author(
    id = this.id,
    name = this.name,
    age = this.age,
    description = this.description,
    image = this.image
)


