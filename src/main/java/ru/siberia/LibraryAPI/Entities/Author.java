package ru.siberia.LibraryAPI.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "author", schema = "v1")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "name",
            nullable = false)
    String name;

    @Column(name = "country",
            nullable = false)
    String country;

    @Column(name = "birth_date")
    LocalDateTime birthdate;

    @Column(name = "die_date")
    LocalDateTime dieDate;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDateTime getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDateTime birthdate) {
        this.birthdate = birthdate;
    }

    public LocalDateTime getDieDate() {
        return dieDate;
    }

    public void setDieDate(LocalDateTime dieDate) {
        this.dieDate = dieDate;
    }
}
