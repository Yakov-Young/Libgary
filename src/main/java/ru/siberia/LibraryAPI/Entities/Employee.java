package ru.siberia.LibraryAPI.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.siberia.LibraryAPI.Entities.Enums.Roles;

import java.time.LocalDateTime;
import java.util.Date;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")

    @Column(name = "start_date",
            nullable = false)
    Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")

    @Column(name = "end_date")
    Date endDate;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")

    @Column(name = "birth_day")
    Date birthday;

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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
