package ru.siberia.LibraryAPI.DTOs;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;
import ru.siberia.LibraryAPI.Entities.Book;
import ru.siberia.LibraryAPI.Entities.Reader;

import java.time.LocalDateTime;
import java.util.Date;

public class GiveDTO {
    String firstName;
    String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


}
