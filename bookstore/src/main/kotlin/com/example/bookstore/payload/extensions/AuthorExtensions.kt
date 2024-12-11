package com.example.bookstore.payload.extensions

import com.example.bookstore.entities.Author
import com.example.bookstore.exceptions.InvalidAuthorException
import com.example.bookstore.payload.dto.author.AuthorDto
import com.example.bookstore.payload.dto.author.AuthorSummaryDto
import com.example.bookstore.payload.dto.author.AuthorUpdateRequestDto
import com.example.bookstore.payload.request.AuthorUpdateRequest
import com.example.bookstore.payload.summary.AuthorSummary

fun Author.toAuthorDto()= AuthorDto(
    id = this.id,
    name = this.name,
    age = this.age,
    description = this.description,
    image = this.image
)

fun Author.toAuthorSummaryDto() : AuthorSummaryDto {
    val authorId = this.id ?: throw InvalidAuthorException()
    return AuthorSummaryDto(
        id = authorId,
        name = this.name,
        image = this.image
    )
}


fun AuthorDto.toAuthor()= Author(
    id = this.id,
    name = this.name,
    age = this.age,
    description = this.description,
    image = this.image
)

fun AuthorUpdateRequestDto.toAuthorUpdateRequest() = AuthorUpdateRequest(
    name = this.name,
    age = this.age,
    description = this.description,
    image = this.image
)

fun AuthorSummaryDto.toAuthorSummary() = AuthorSummary(
    id = this.id,
    name = this.name,
    image = this.image
)