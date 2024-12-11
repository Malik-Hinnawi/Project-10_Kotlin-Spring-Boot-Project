package com.example.bookstore.payload.dto

data class AuthorDto(
     var id: Long?,
     var name: String,
     var age: Int,
     var description: String,
     var image: String
)