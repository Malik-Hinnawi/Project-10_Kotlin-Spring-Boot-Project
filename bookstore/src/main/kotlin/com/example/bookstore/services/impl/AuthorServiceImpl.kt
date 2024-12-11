package com.example.bookstore.services.impl

import com.example.bookstore.entities.Author
import com.example.bookstore.payload.request.AuthorUpdateRequest
import com.example.bookstore.repositories.AuthorRepository
import com.example.bookstore.services.AuthorService
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository
): AuthorService {
    override fun create(author: Author): Author {
        require(null == author.id)
        return authorRepository.save(author)
    }

    override fun list(): List<Author> {
        return authorRepository.findAll()
    }

    override fun getById(id: Long): Author? {
        return authorRepository.findByIdOrNull(id)
    }

    @Transactional
    override fun fullUpdate(id: Long, author: Author): Author {
        check(authorRepository.existsById(id))
        val normalizedAuthor = author.copy(id=id)
        return authorRepository.save(normalizedAuthor)
    }

    @Transactional
    override fun partialUpdate(id: Long, authorUpdate: AuthorUpdateRequest): Author {
        val exisingAuthor = authorRepository.findByIdOrNull(id)
        checkNotNull(exisingAuthor)
        val updatedAuthor = exisingAuthor.copy(
            name = authorUpdate.name ?:  exisingAuthor.name,
            age = authorUpdate.age ?: exisingAuthor.age,
            description = authorUpdate.description ?: exisingAuthor.description,
            image = authorUpdate.image ?: exisingAuthor.image
        )

        return authorRepository.save(updatedAuthor)
    }

    override fun delete(id: Long) {
        authorRepository.deleteById(id)
    }


}