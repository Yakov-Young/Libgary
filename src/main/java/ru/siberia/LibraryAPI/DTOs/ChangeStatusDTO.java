package ru.siberia.LibraryAPI.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.siberia.LibraryAPI.Entities.Enums.Status;

public class ChangeStatusDTO {
    public Status getStatus() {
        return status;
    }

    @JsonProperty("status")
    Status status;
}
