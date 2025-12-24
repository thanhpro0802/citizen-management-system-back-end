package com.citizen.management.citizen_management_system_back_end.entity;

import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThaiNhanKhau;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "nhan_khau")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class NhanKhau {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    @Column(name = "trang_thai")
    private EnumTrangThaiNhanKhau trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_ho_khau")
    @JsonIgnore // [THÊM DÒNG NÀY]: Ngắt vòng lặp khi xem NhanKhau sẽ không load lại HoKhau
    private HoKhau hoKhau;

    @OneToMany(mappedBy = "nhanKhau", fetch = FetchType.LAZY)
    @JsonIgnore // [THÊM DÒNG NÀY]: Nên ẩn danh sách tạm trú khi load nhân khẩu để tránh nặng
    private List<TamTru> danhSachTamTru;

    @OneToMany(mappedBy = "nhanKhau", fetch = FetchType.LAZY)
    @JsonIgnore // [THÊM DÒNG NÀY]: Nên ẩn danh sách tạm vắng khi load nhân khẩu để tránh nặng
    private List<TamVang> danhSachTamVang;

    @OneToOne(mappedBy = "nhanKhau", fetch = FetchType.LAZY)
    @JsonIgnore // [THÊM DÒNG NÀY]: Nên ẩn thông tin tài khoản khi load nhân khẩu để tránh lộ thông tin nhạy cảm
    private TaiKhoan taiKhoan;

}
