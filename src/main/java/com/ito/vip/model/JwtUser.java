package com.ito.vip.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUser implements UserDetails {
    private Long pkId;
    private String username;
    private String password;
    private Boolean isEnabled;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtUser(Long pkId, String username, String password, Boolean isEnabled,
            Collection<? extends GrantedAuthority> authorities) {
        this.pkId = pkId;
        this.username = username;
        this.password = password;
        this.isEnabled = isEnabled;
        this.authorities = authorities;
    }

    public Long getPkId() {
        return pkId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Default set as true, should be set basing on business
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Default set as true, should be set basing on business
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Default set as true, should be set basing on business
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
