package ru.siberia.LibraryAPI.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import ru.siberia.LibraryAPI.Entities.Enums.Categories;

@Entity
@Table(name = "book", schema = "v1")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "title",
            nullable = false)
    String title;

    @OneToOne()
    @JsonIncludeProperties(value = {"id", "name"})
    @JoinColumn(name = "author_id",
            referencedColumnName = "id",
            nullable = false)
    Author author;

    @Column(name = "isbn",
            unique = true,
            nullable = false)
    String isbn;

    @Column(name = "description",
            nullable = false)
    String description;

    @Column(name = "page_count",
            nullable = false)
    int pageCount;

    @Column(name = "category",
            nullable = false)
    @Enumerated(EnumType.ORDINAL)
    Categories category;

    @Column(name = "available")
    boolean available;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }
}
