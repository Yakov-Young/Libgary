package ru.siberia.LibraryAPI.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "access", schema = "v1")
public class Access {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "login",
            unique = true,
            nullable = false)
    String login;

    @Column(name = "password",
            nullable = false)
    String password;

    public Long getId() {
        return id;
    }

    public Access() {
    }

    public Access(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
