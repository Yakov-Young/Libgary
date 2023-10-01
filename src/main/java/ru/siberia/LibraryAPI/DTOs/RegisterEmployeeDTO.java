package ru.siberia.LibraryAPI.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import ru.siberia.LibraryAPI.Entities.Access;
import ru.siberia.LibraryAPI.Entities.Enums.Roles;

import java.time.LocalDateTime;

public class RegisterEmployeeDTO {
    @JsonProperty("firstName")
    String firstName;
    @JsonProperty("lastName")
    String lastName;
    @JsonProperty("salary")
    int salary;
    @JsonProperty("login")
    String login;
    @JsonProperty("password")
    String password;
    @JsonProperty("access")
    Access access;
    @JsonProperty("role")
    Roles role;
    @JsonProperty("birthday")
    LocalDateTime birthday;

    public LocalDateTime getBirthday() {
        return birthday;
    }
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

    public int getSalary() {
        return salary;
    }

    public Roles getRole() {
        return role;
    }

    public Access getAccess() {
        return access;
    }

}
