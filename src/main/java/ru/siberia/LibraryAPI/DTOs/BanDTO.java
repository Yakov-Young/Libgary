package ru.siberia.LibraryAPI.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BanDTO {

    @JsonProperty("IsBanned")
    boolean isBanned;

    public boolean isBanned() {
        return isBanned;
    }
}
