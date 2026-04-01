package com.errolito.mycrud.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
//@JsonPropertyOrder({"id", "username", "createdDate", "userProfile"})
public class UserResponse {
    private Integer id;
    private String username;
    private Instant createdDate;

    private UserUserProfileResponse userProfile;
}
