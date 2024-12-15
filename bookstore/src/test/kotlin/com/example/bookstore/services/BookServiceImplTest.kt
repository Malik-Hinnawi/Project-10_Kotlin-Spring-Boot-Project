package com.example.bookstore.services

import com.example.bookstore.BOOK_A_ISBN
import com.example.bookstore.payload.request.BookUpdateRequest
import com.example.bookstore.payload.summary.AuthorSummary
import com.example.bookstore.repositories.AuthorRepository
import com.example.bookstore.repositories.BookRepository
import com.example.bookstore.services.impl.BookServiceImpl
import com.example.bookstore.testAuthorEntityA
import com.example.bookstore.testBookEntityA
import com.example.bookstore.testBookSummaryA
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.`as`
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
@Transactional
class BookServiceImplTest @Autowired constructor(
    private val underTest: BookServiceImpl,
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository
){
    @AfterEach
    fun clearDb(){
        bookRepository.deleteAll()
        authorRepository.deleteAll()
    }

    @Test
    fun `test that create update throws IllegalStateException when author does not exist`(){
        val authorSummary = AuthorSummary(1)
        val bookSummary = testBookSummaryA(BOOK_A_ISBN, authorSummary)
        assertThrows<IllegalStateException> {
            underTest.createUpdate(BOOK_A_ISBN,  bookSummary)
        }
    }

    @Test
    fun `test that createUpdate successfully create a book in database`(){
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        assertThat(savedAuthor).isNotNull()
        val authorSummary = AuthorSummary(savedAuthor.id!!)
        val bookSummary = testBookSummaryA(BOOK_A_ISBN, authorSummary)
        val (savedBook, isCreated) = underTest.createUpdate(BOOK_A_ISBN,bookSummary)
        assertThat(savedBook).isNotNull()

        val recalledBook = bookRepository.findByIdOrNull(BOOK_A_ISBN)
        assertThat(recalledBook).isNotNull()
        assertThat(recalledBook).isEqualTo(savedBook)
        assertThat(isCreated).isTrue()
    }

    @Test
    fun `test that createUpdate successfully updates a book in database`(){
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        assertThat(savedAuthor).isNotNull()
        val savedBook = bookRepository.save(testBookEntityA(BOOK_A_ISBN, savedAuthor))
        assertThat(savedBook).isNotNull()

        val authorSummary = AuthorSummary(savedAuthor.id!!)
        val bookSummary = testBookSummaryA(BOOK_A_ISBN, authorSummary).copy(description = "updated description")
        val (updatedBook, isCreated) = underTest.createUpdate(BOOK_A_ISBN,bookSummary)
        assertThat(updatedBook).isNotNull()

        val recalledBook = bookRepository.findByIdOrNull(BOOK_A_ISBN)
        assertThat(recalledBook).isNotNull()
        assertThat(recalledBook).isEqualTo(updatedBook)
        assertThat(isCreated).isFalse()
    }

    @Test
    fun `test that list returns an empty list when no book in the database`(){
        val result = underTest.list()
        assertThat(result).isEmpty()
    }

    @Test
    fun `test that list return a list of book when books are in database`(){
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        assertThat(savedAuthor).isNotNull()
        val savedBook = bookRepository.save(testBookEntityA(BOOK_A_ISBN, savedAuthor))
        assertThat(savedBook).isNotNull()

        val result = underTest.list()
        assertThat(result).isNotEmpty()
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(savedBook)
    }

    @Test
    fun `test that list returns no books when the author Id not match`(){
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        assertThat(savedAuthor).isNotNull()
        val savedBook = bookRepository.save(testBookEntityA(BOOK_A_ISBN, savedAuthor))
        assertThat(savedBook).isNotNull()

        val result = underTest.list(authorId = savedAuthor.id?.let { it + 1 })
        assertThat(result).isEmpty()
    }

    @Test
    fun `test that list returns books when the author Id match`(){
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        assertThat(savedAuthor).isNotNull()
        val savedBook = bookRepository.save(testBookEntityA(BOOK_A_ISBN, savedAuthor))
        assertThat(savedBook).isNotNull()

        val result = underTest.list(authorId = savedAuthor.id)
        assertThat(result).isNotEmpty()
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(savedBook)
    }


    @Test
    fun `test that get returns null when book not found in the database`(){
        val result = underTest.get(isbn = BOOK_A_ISBN)
        assertThat(result).isNull()
    }

    @Test
    fun `test that get returns book when book found in the database`(){
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        assertThat(savedAuthor).isNotNull()
        val savedBook = bookRepository.save(testBookEntityA(BOOK_A_ISBN, savedAuthor))
        assertThat(savedBook).isNotNull()

        val result = underTest.get(isbn = BOOK_A_ISBN)
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(savedBook)
    }

    @Test
    fun `test that partialUpdate throws IllegalStateException when the Book does not exist in the db`(){
        val bookUpdateRequest = BookUpdateRequest(title = "New title")
        assertThrows<IllegalStateException> {
            underTest.partialUpdate(isbn = BOOK_A_ISBN, bookUpdateRequest)
        }
    }

    @Test
    fun `test that partialUpdate updates the title of an existing book`(){
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        assertThat(savedAuthor).isNotNull()
        val savedBook = bookRepository.save(testBookEntityA(BOOK_A_ISBN, savedAuthor))
        assertThat(savedBook).isNotNull()



        val newTitle = "New title"
        val expectedBook = savedBook.copy(title = newTitle)
        val bookUpdateRequest = BookUpdateRequest(title = newTitle)

        val result = underTest.partialUpdate(isbn = BOOK_A_ISBN, bookUpdateRequest)
        assertThat(result.title).isEqualTo(newTitle)
        assertThat(result).isEqualTo(expectedBook)
    }

    @Test
    fun `test that partialUpdate updates the description of an existing book`(){
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        assertThat(savedAuthor).isNotNull()
        val savedBook = bookRepository.save(testBookEntityA(BOOK_A_ISBN, savedAuthor))
        assertThat(savedBook).isNotNull()

        val newDescription = "New description"
        val expectedBook = savedBook.copy(description = newDescription)
        val bookUpdateRequest = BookUpdateRequest(description = newDescription)

        val result = underTest.partialUpdate(isbn = BOOK_A_ISBN, bookUpdateRequest)
        assertThat(result.description).isEqualTo(newDescription)
        assertThat(result).isEqualTo(expectedBook)
    }

    @Test
    fun `test that partialUpdate updates the image of an existing book`(){
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        assertThat(savedAuthor).isNotNull()
        val savedBook = bookRepository.save(testBookEntityA(BOOK_A_ISBN, savedAuthor))
        assertThat(savedBook).isNotNull()



        val newImage = "new_image.jpg"
        val expectedBook = savedBook.copy(image = newImage)
        val bookUpdateRequest = BookUpdateRequest(image = newImage)

        val result = underTest.partialUpdate(isbn = BOOK_A_ISBN, bookUpdateRequest)
        assertThat(result.image).isEqualTo(newImage)
        assertThat(result).isEqualTo(expectedBook)
    }


    @Test
    fun `test that delete successfully deletes a book that exists in the database`(){
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        assertThat(savedAuthor).isNotNull()
        val savedBook = bookRepository.save(testBookEntityA(BOOK_A_ISBN, savedAuthor))
        assertThat(savedBook).isNotNull()

        underTest.delete(BOOK_A_ISBN)
        val result = bookRepository.findByIdOrNull(BOOK_A_ISBN)
        assertThat(result).isNull()
    }

    @Test
    fun `test that delete successfully deletes a book that does not exist in the database`(){
        underTest.delete(BOOK_A_ISBN)
        val result = bookRepository.findByIdOrNull(BOOK_A_ISBN)
        assertThat(result).isNull()
    }

}