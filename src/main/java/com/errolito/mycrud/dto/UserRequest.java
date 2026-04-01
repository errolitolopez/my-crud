package com.errolito.mycrud.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequest {
    @Pattern(
            regexp = "(?!.*[.\\-_]{2,})^[a-zA-Z0-9.\\-_]{5,32}$",
            message = "Username must be 5–20 characters long, contain only letters, numbers, dots (.), dashes (-), or" +
                    " underscores (_), and cannot have consecutive dots, dashes, or underscores."
    )
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Full name is required")
    private String fullName;

    public String getUsername() {
        return username != null ? username.toLowerCase() : null;
    }
}