package ru.siberia.LibraryAPI.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

public class WorkedStatusDTO {
    @JsonProperty("worked")
    private boolean isWorked;
    public boolean isWorked() {
        return isWorked;
    }
}
