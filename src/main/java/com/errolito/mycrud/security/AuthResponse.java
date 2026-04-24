package com.errolito.mycrud.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private List<String> roles;
    private List<String> permissions;
}
