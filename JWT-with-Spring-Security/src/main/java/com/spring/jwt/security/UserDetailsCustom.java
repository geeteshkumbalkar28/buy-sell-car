package com.spring.jwt.security;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsCustom implements UserDetails {

    private String username;

    private String password;

    private String firstName;

    private String dealerId;

    private String userId;

    private  String userProfileId;


    public String getUserId() {
        return userId;
    }

    public String getUserProfileId() {
        return userProfileId;
    }

    public String getDealerId() {
        return dealerId;
    }

    public String getFirstName() {
        return firstName;
    }


    private List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
