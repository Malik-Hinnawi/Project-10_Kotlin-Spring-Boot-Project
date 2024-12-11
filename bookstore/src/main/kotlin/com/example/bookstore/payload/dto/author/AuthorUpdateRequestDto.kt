package com.example.bookstore.payload.dto.author

data class AuthorUpdateRequestDto(
     var name: String? = null,
     var age: Int? = null,
     var description: String? = null,
     var image: String? = null
)