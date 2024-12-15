package com.example.bookstore.entities

import jakarta.persistence.*

@Entity
@Table(name = "authors")
data class Author(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long?,
    @Column(name = "name")
    var name: String,
    @Column(name = "age")
    var age: Int,
    @Column(name = "description")
    var description: String,
    @Column(name = "image")
    var image: String,
    @OneToMany(mappedBy = "author", cascade = [CascadeType.REMOVE])
    val bookEntities: List<Book> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Author

        if (id != other.id) return false
        if (name != other.name) return false
        if (age != other.age) return false
        if (description != other.description) return false
        if (image != other.image) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + age
        result = 31 * result + description.hashCode()
        result = 31 * result + image.hashCode()
        return result
    }
}