package com.example.bookstore.services

import com.example.bookstore.entities.Author
import com.example.bookstore.payload.dto.author.AuthorUpdateRequestDto
import com.example.bookstore.payload.extensions.toAuthorUpdateRequest
import com.example.bookstore.payload.request.AuthorUpdateRequest
import com.example.bookstore.repositories.AuthorRepository
import com.example.bookstore.services.impl.AuthorServiceImpl
import com.example.bookstore.testAuthorEntityA
import com.example.bookstore.testAuthorEntityB
import com.example.bookstore.testAuthorUpdateRequestA
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.client.RestClient

@SpringBootTest
@Transactional
class AuthorServiceImplTest @Autowired constructor(
    private val underTest: AuthorServiceImpl,
    private val authorRepository: AuthorRepository
    ) {

    @Autowired
    private lateinit var restClientBuilder: RestClient.Builder

    @AfterEach
    fun clearDb(){
        authorRepository.deleteAll()
    }

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
    fun `test that an Author with an ID throws an IllegalArgumentException`(){
        assertThrows<IllegalArgumentException> {
            val existingAuthor = testAuthorEntityA(id = 999)
            underTest.create(existingAuthor)
        }
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

    @Test
    fun `test that full update successfully updates the author in the db`(){
        val existingAuthor = authorRepository.save(testAuthorEntityA())
        val existingAuthorId = existingAuthor.id!!
        val updatedAuthor = testAuthorEntityB(id= existingAuthorId)

        val result = underTest.fullUpdate(existingAuthorId, updatedAuthor)
        assertThat(result).isEqualTo(updatedAuthor)
        val retrievedAuthor = authorRepository.findByIdOrNull(existingAuthorId)
        assertThat(retrievedAuthor).isNotNull()
        assertThat(retrievedAuthor).isEqualTo(updatedAuthor)
    }

    @Test
    fun `test that full update Author throws IllegalStateException when author does no exist in db`(){
        assertThrows<IllegalStateException> {
            val nonExistingAuthorId = 10L
            val updatedAuthor = testAuthorEntityB(id = nonExistingAuthorId)
            underTest.fullUpdate(nonExistingAuthorId, updatedAuthor)
        }
    }

    @Test
    fun `test that partial update Author throws IllegalStateException when author does no exist in db`(){
        assertThrows<IllegalStateException> {
            val nonExistingAuthorId = 10L
            val updatedRequest = testAuthorUpdateRequestA()
            underTest.partialUpdate(nonExistingAuthorId, updatedRequest)
        }
    }

    @Test
    fun `test that partial update Author does not update Author when all values are null`(){
        val existingAuthor = authorRepository.save(testAuthorEntityA())
        val updatedAuthor =  underTest.partialUpdate(existingAuthor.id!!, AuthorUpdateRequestDto().toAuthorUpdateRequest())
        assertThat(updatedAuthor).isEqualTo(existingAuthor)
    }

    @Test
    fun `test that partial update Author updates author name`(){
        val existingAuthor = testAuthorEntityA()
        val updateRequest = AuthorUpdateRequestDto(name = "newName").toAuthorUpdateRequest()
        val expected = existingAuthor.copy(name = "newName")
        assertThatAuthorPartialUpdateIsUpdated(testAuthorEntityA(), expected, updateRequest)
    }

    @Test
    fun `test that partial update Author updates author age`(){
        val existingAuthor = testAuthorEntityA()
        val updateRequest = AuthorUpdateRequestDto(age = 15).toAuthorUpdateRequest()
        val expected = existingAuthor.copy(age = 15)
        assertThatAuthorPartialUpdateIsUpdated(testAuthorEntityA(), expected, updateRequest)
    }

    @Test
    fun `test that partial update Author updates author description`(){
        val existingAuthor = testAuthorEntityA()
        val updateRequest = AuthorUpdateRequestDto(description = "newDescription").toAuthorUpdateRequest()
        val expected = existingAuthor.copy(description = "newDescription")
        assertThatAuthorPartialUpdateIsUpdated(testAuthorEntityA(), expected, updateRequest)
    }

    @Test
    fun `test that partial update Author updates author image`(){
        val existingAuthor = testAuthorEntityA()
        val updateRequest = AuthorUpdateRequestDto(image = "newImage.jpg").toAuthorUpdateRequest()
        val expected = existingAuthor.copy(image = "newImage.jpg")
        assertThatAuthorPartialUpdateIsUpdated(testAuthorEntityA(), expected, updateRequest)
    }

    @Test
    fun `test that delete deletes an existing Author in the db`(){
        val existingAuthor = authorRepository.save(testAuthorEntityA())
        underTest.delete(existingAuthor.id!!)

        assertThat(authorRepository.existsById(existingAuthor.id!!)).isFalse()
    }

    @Test
    fun `test that delete deletes a non existing Author in the db`(){
        val nonExistingId = 999L
        underTest.delete(nonExistingId)

        assertThat(authorRepository.existsById(nonExistingId)).isFalse()
    }




    private fun assertThatAuthorPartialUpdateIsUpdated(
        existingAuthor : Author,
        expectedAuthor: Author,
        authorUpdateRequest : AuthorUpdateRequest
    ){
        val savedExistingAuthor = authorRepository.save(existingAuthor)
        val updatedAuthor =  underTest.partialUpdate(existingAuthor.id!!, authorUpdateRequest)

        val expected = expectedAuthor.copy(id=savedExistingAuthor.id)
        assertThat(updatedAuthor).isEqualTo(expected)

        val retrievedAuthor = authorRepository.findByIdOrNull(existingAuthor.id)
        assertThat(retrievedAuthor).isNotNull()
        assertThat(retrievedAuthor).isEqualTo(expected)
    }


}