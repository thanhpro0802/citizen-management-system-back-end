package com.citizen.management.citizen_management_system_back_end.security.services;

import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private String id;
    private String username; // Trong hệ thống này, username chính là CCCD
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String id, String username, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(TaiKhoan user) {
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getVaiTro().name()));

        return new UserDetailsImpl(
                user.getMaTaiKhoan(),
                user.getCccd(), // SỬA: Lấy CCCD làm username
                user.getMatKhau(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    public String getId() { return id; }
    @Override
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return username; } // Trả về CCCD
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}