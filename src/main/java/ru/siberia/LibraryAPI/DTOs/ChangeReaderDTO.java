package ru.siberia.LibraryAPI.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import ru.siberia.LibraryAPI.Entities.Access;

import java.time.LocalDateTime;

public class ChangeReaderDTO {
    @JsonProperty("firstName")
    String firstName;
    @JsonProperty("lastName")
    String lastName;
    @JsonProperty("birthday")
    LocalDateTime birthday;
    @JsonProperty("address")
    String address;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }
}
