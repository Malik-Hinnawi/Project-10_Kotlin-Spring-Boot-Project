package com.example.bookstore.services

import com.example.bookstore.repositories.AuthorRepository
import com.example.bookstore.services.impl.AuthorServiceImpl
import com.example.bookstore.testAuthorEntityA
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.client.RestClient

@SpringBootTest
class AuthorServiceImplTest @Autowired constructor(
    private val underTest: AuthorServiceImpl,
    private val authorRepository: AuthorRepository
    ) {

    @Autowired
    private lateinit var restClientBuilder: RestClient.Builder

    @Test
    fun `test that save persists the Author in the database`(){
        val savedAuthor = underTest.create(testAuthorEntityA())
        assertThat(savedAuthor.id).isNotNull()

        val recalledAuthor = authorRepository.findByIdOrNull(savedAuthor.id!!)
        assertThat(recalledAuthor).isNotNull()

        assertThat(recalledAuthor!!).isEqualTo(
            testAuthorEntityA(id = savedAuthor.id)
        )

    }

    @Test
    fun `test that list returns empty list when no authors in database`(){
        val result = underTest.list()
        assertThat(result).isEmpty()
    }

    @Test
    fun `test that list returns list when authors in database`(){
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        val expected = listOf(savedAuthor)
        val result = underTest.list()
        assertThat(result).isNotEmpty()
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `test that get return null when author is not present in db`(){
        val result = underTest.getById(999)
        assertThat(result).isNull()
    }

    @Test
    fun `test that get return author when author is present in db`(){
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        val result = underTest.getById(savedAuthor.id!!)
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(savedAuthor)
    }

}