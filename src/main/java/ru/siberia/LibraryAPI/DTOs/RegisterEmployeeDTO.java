package ru.siberia.LibraryAPI.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.siberia.LibraryAPI.Entities.Access;
import ru.siberia.LibraryAPI.Entities.Enums.Roles;

import java.time.LocalDateTime;
import java.util.Date;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("birthday")
    Date birthday;

    public Date getBirthday() {
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
