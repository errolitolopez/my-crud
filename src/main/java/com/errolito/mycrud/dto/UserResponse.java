package com.errolito.mycrud.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class UserResponse {
    private Integer id;
    private String username;
    private Instant createdDate;

    private UserUserProfileResponse userProfile;
}