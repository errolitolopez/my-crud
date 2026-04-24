package com.errolito.mycrud.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@ToString
public class CustomUserDetails implements UserDetails {
    private Integer id;
    private String email;
    private String username;
    private String password;
    private Collection<GrantedAuthority> authorities;
}