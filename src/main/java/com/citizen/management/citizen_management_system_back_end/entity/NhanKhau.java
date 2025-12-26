package com.citizen.management.citizen_management_system_back_end.entity;

import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThaiNhanKhau;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;

@Entity
@Table(name = "nhan_khau")
@Getter
@Setter
public class NhanKhau {

    @Id
    @Column(name = "ma_nhan_khau")
    private String maNhanKhau;

    @Column(name = "ho_ten")
    private String hoTen;

    @Column(name = "ngay_sinh")
    private Date ngaySinh;

    @Column(name = "gioi_tinh")
    private String gioiTinh;

    @Column(name = "so_cccd", unique = true)
    private String soCCCD;

    @Column(name = "que_quan")
    private String queQuan;

    @Column(name = "dan_toc")
    private String danToc;

    @Column(name = "quan_he_voi_chu_ho")
    private String quanHeVoiChuHo;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "trang_thai", nullable = false)
    private EnumTrangThaiNhanKhau trangThai;

    // SỬA: Khi lấy NhanKhau -> Load HoKhau nhưng bỏ qua danhSachThanhVien của hộ đó để tránh loop
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_ho_khau")
    @JsonIgnoreProperties({"danhSachThanhVien", "chuHo", "hibernateLazyInitializer", "handler"})
    private HoKhau hoKhau;

    // SỬA: Bỏ qua danh sách này khi load nhân khẩu để tránh quá tải
    @OneToMany(mappedBy = "nhanKhau", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TamTru> danhSachTamTru;

    // SỬA: Bỏ qua danh sách này khi load nhân khẩu
    @OneToMany(mappedBy = "nhanKhau", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TamVang> danhSachTamVang;

    // SỬA: Bỏ qua tài khoản để tránh loop NhanKhau <-> TaiKhoan
    @OneToOne(mappedBy = "nhanKhau", fetch = FetchType.LAZY)
    @JsonIgnore
    private TaiKhoan taiKhoan;

}