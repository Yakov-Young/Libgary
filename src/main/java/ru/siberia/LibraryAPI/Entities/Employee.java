package ru.siberia.LibraryAPI.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ru.siberia.LibraryAPI.Entities.Enums.Roles;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee", schema = "v1")
public class Employee implements IUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "first_name",
            nullable = false)
    String firstName;

    @Column(name = "last_name",
            nullable = false)
    String lastName;

    @Column(name = "salary",
            nullable = false)
    int salary;

    @Column(name = "start_date",
            nullable = false)
    LocalDateTime startDate;

    @Column(name = "end_date")
    LocalDateTime endDate;

    @Column(name = "role",
            nullable = false)
    Roles role;

    @Column(name = "is_worked",
            nullable = false)
    boolean isWorked;

    @OneToOne()
    @JoinColumn(name = "access_id",
            referencedColumnName = "id",
            nullable = false,
            unique = true)
    @JsonIgnore
    Access access;

    @Column(name = "birth_day")
    LocalDateTime birthday;

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isWorked() {
        return isWorked;
    }

    public void setWorked(boolean worked) {
        isWorked = worked;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
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

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
