package ru.siberia.LibraryAPI.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteBookDTO {
    @JsonProperty("available")
    boolean available;

    public boolean isAvailable() {
        return available;
    }
}
