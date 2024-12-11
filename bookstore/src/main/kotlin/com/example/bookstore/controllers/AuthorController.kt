package com.example.bookstore.controllers

import com.example.bookstore.payload.dto.AuthorDto
import com.example.bookstore.payload.extensions.toAuthor
import com.example.bookstore.payload.extensions.toAuthorDto
import com.example.bookstore.services.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
        @RequestBody authorDto: AuthorDto) : ResponseEntity<AuthorDto>{
        try {
            val updatedEntity = authorService.fullUpdate(id, authorDto.toAuthor())
            return ResponseEntity(updatedEntity.toAuthorDto(), HttpStatus.OK)
        } catch (e: IllegalArgumentException){
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

}