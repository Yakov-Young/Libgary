package ru.siberia.LibraryAPI.JwtFilter.Error;

public class Unauthentication extends RuntimeException {
    public Unauthentication(String message) {
        super(message);
    }
}
