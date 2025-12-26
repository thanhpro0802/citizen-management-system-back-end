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
    private String username;
    @JsonIgnore
    private String password;

    // --- CÁC BIẾN MỚI CẦN THÊM ---
    private String soDienThoai;  // <--- Quan trọng nhất
    private String hoTen;
    private String email;
    // -----------------------------

    private Collection<? extends GrantedAuthority> authorities;

    // --- SỬA CONSTRUCTOR ĐỂ NHẬN THÊM THAM SỐ ---
    public UserDetailsImpl(String id, String username, String password,
                           String soDienThoai, String hoTen, String email, // <--- Thêm 3 tham số này
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.soDienThoai = soDienThoai; // <--- Gán giá trị vào biến
        this.hoTen = hoTen;
        this.email = email;
        this.authorities = authorities;
    }

    // --- SỬA HÀM BUILD ĐỂ LẤY DỮ LIỆU TỪ ENTITY ---
    public static UserDetailsImpl build(TaiKhoan user) {
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getVaiTro().name()));

        // Lấy họ tên an toàn (tránh null)
        String hoTen = "";
        if (user.getNhanKhau() != null) {
            hoTen = user.getNhanKhau().getHoTen();
        }

        return new UserDetailsImpl(
                user.getMaTaiKhoan(),
                user.getCccd(),
                user.getMatKhau(),
                user.getSoDienThoai(), // <--- CHÍNH LÀ CHỖ NÀY! Lấy từ Entity truyền vào Constructor
                hoTen,
                "", // Email (để trống nếu chưa có)
                authorities);
    }

    // --- CÁC HÀM GETTER MỚI (Để AuthenticationService gọi được) ---
    public String getSoDienThoai() { return soDienThoai; }
    public String getHoTen() { return hoTen; }
    public String getEmail() { return email; }
    // -------------------------------------------------------------

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    public String getId() { return id; }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}