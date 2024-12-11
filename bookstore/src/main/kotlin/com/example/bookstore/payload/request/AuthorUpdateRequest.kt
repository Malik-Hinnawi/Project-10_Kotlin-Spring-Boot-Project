package com.example.bookstore.payload.request

data class AuthorUpdateRequest(
     var name: String?,
     var age: Int?,
     var description: String?,
     var image: String?
)