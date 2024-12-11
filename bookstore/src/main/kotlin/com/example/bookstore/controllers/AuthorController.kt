package com.example.bookstore.controllers

import com.example.bookstore.payload.dto.author.AuthorDto
import com.example.bookstore.payload.dto.author.AuthorUpdateRequestDto
import com.example.bookstore.payload.extensions.toAuthor
import com.example.bookstore.payload.extensions.toAuthorDto
import com.example.bookstore.payload.extensions.toAuthorUpdateRequest
import com.example.bookstore.services.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/v1/authors"])
class AuthorController(
    private val authorService: AuthorService,
) {
    @PostMapping
    fun createAuthor(@RequestBody authorDto: AuthorDto) : ResponseEntity<AuthorDto>{
        try {
            val createdAuthor = authorService.create(authorDto.toAuthor()).toAuthorDto()
            return ResponseEntity(createdAuthor, HttpStatus.CREATED)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping
    fun getAllAuthors(): ResponseEntity<List<AuthorDto>> {
        val authors = authorService.list().map { it.toAuthorDto() }
        return ResponseEntity.ok(authors)
    }

    @GetMapping("/{id}")
    fun getAuthorById(@PathVariable(name = "id") id : Long): ResponseEntity<AuthorDto>{
        val authorFound = authorService.getById(id)?.toAuthorDto()
        return authorFound?.let {
            ResponseEntity(it, HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{id}")
    fun fullUpdate(
        @PathVariable(name = "id") id: Long,
        @RequestBody authorDto: AuthorDto
    ) : ResponseEntity<AuthorDto>{
        try {
            val updatedEntity = authorService.fullUpdate(id, authorDto.toAuthor())
            return ResponseEntity(updatedEntity.toAuthorDto(), HttpStatus.OK)
        } catch (e: IllegalStateException){
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PatchMapping("/{id}")
    fun partialUpdate(
        @PathVariable(name = "id") id: Long,
        @RequestBody authorUpdateDto: AuthorUpdateRequestDto
    ) : ResponseEntity<AuthorDto>
    {
        return try {
            val updatedEntity = authorService.partialUpdate(id, authorUpdateDto.toAuthorUpdateRequest())
             ResponseEntity(updatedEntity.toAuthorDto(), HttpStatus.OK)
        } catch (e: IllegalStateException){
             ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteAuthor(@PathVariable(name = "id") id: Long): ResponseEntity<Unit>{
        authorService.delete(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}