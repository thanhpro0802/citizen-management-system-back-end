package com.citizen.management.citizen_management_system_back_end.entity;

import com.citizen.management.citizen_management_system_back_end.enums.EnumVaiTro;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "tai_khoan")
@Getter
@Setter
public class TaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ma_tai_khoan")
    private String maTaiKhoan;

    // Tên đăng nhập chính là số CCCD
    @Column(name = "so_cccd", unique = true, nullable = false)
    private String cccd;

    @Column(name = "mat_khau")
    private String matKhau;

    // --- THÊM CỘT SỐ ĐIỆN THOẠI ---
    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Enumerated(EnumType.STRING)
    @Column(name = "vai_tro")
    private EnumVaiTro vaiTro;

    // --- Mối quan hệ (Relationships) ---

    // Quan hệ 1:1 với NhanKhau
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nhan_khau_id", referencedColumnName = "ma_nhan_khau")
    @JsonIgnoreProperties(value = {"hoKhau", "thanhVienCuaHo", "taiKhoan", "hibernateLazyInitializer", "handler"})
    private NhanKhau nhanKhau;

    @JsonIgnore
    @OneToMany(mappedBy = "nguoiGui", fetch = FetchType.LAZY)
    private List<PhanAnh> phanAnhDaGui;

    @JsonIgnore
    @OneToMany(mappedBy = "canBoPhuTrach", fetch = FetchType.LAZY)
    private List<PhanAnh> phanAnhDaXuLy;

    @JsonIgnore
    @OneToMany(mappedBy = "taiKhoanThucHien", fetch = FetchType.LAZY)
    private List<LichSuPhanAnh> lichSuDaThucHien;

    public TaiKhoan() {
    }
}