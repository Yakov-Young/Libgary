package ru.siberia.LibraryAPI.DTOs;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import ru.siberia.LibraryAPI.Entities.Author;
import ru.siberia.LibraryAPI.Entities.Enums.Categories;

public class CreateBookDTO {
    @JsonProperty("title")
    String title;
    @JsonProperty("author")
    String author;
    @JsonProperty("isbn")
    String isbn;
    @JsonProperty("description")
    String description;
    @JsonProperty("category")
    Categories category;

    public Categories getCategory() {
        return category;
    }

    @JsonProperty("pageCount")
    int pageCount;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDescription() {
        return description;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }
}
