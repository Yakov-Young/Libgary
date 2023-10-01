package ru.siberia.LibraryAPI.Entities;

import jakarta.persistence.*;
import ru.siberia.LibraryAPI.Entities.Enums.Status;

import java.time.LocalDateTime;

@Entity
@Table(name = "borrow_book", schema = "v1")
public class BorrowBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "reader_id",
                nullable = false)
    Reader reader;

    @ManyToOne
    @JoinColumn(name = "book_id",
                nullable = false)
    Book book;

    @Column(name = "start_date",
            nullable = false)
    LocalDateTime startDate;

    @Column(name = "end_date",
            nullable = false)
    LocalDateTime endDate;

    @Column(name = "status",
            nullable = false)
    @Enumerated(EnumType.ORDINAL)
    Status status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
