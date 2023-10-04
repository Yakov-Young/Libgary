package ru.siberia.LibraryAPI.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "reader", schema = "v1")
public class Reader implements IUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "first_name",
            nullable = false)
    String firstName;

    @Column(name = "last_name",
            nullable = false)
    String lastName;

    @Column(name = "birthday",
            nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    Date birthday;

    @Column(name = "is_banned",
            nullable = false)
    boolean isBanned;

    @OneToOne
    @JoinColumn(name = "access_id",
                referencedColumnName = "id",
                //nullable = false,
                unique = true)
    @JsonIgnore
    Access access;

    @Column(name = "address",
            nullable = false)
    String address;

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

    public String getAddress() {
        return address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
