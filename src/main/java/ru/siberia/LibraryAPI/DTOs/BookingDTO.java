package ru.siberia.LibraryAPI.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import ru.siberia.LibraryAPI.Entities.Book;
import ru.siberia.LibraryAPI.Entities.Enums.Status;
import ru.siberia.LibraryAPI.Entities.Reader;

import java.time.LocalDateTime;

public class BookingDTO {

    public int getBook() {
        return book;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Status getStatus() {
        return status;
    }

    @JsonProperty("book_id")
    int book;
    @JsonProperty("startDate")
    LocalDateTime startDate;
    @JsonProperty("endDate")
    LocalDateTime endDate;
    @JsonProperty("status")
    Status status;
}
