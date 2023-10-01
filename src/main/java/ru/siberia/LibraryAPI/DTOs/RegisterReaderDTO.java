package ru.siberia.LibraryAPI.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class RegisterReaderDTO {

    @JsonProperty("firstName")
    String firstName;
    @JsonProperty("lastName")
    String lastName;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("birthday")
    LocalDateTime birthday;
    @JsonProperty("isBanned")
    boolean isBanned;
    @JsonProperty("address")
    String address;
    @JsonProperty("login")
    String login;
    @JsonProperty("password")
    String password;

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public String getAddress() {
        return address;
    }
}
