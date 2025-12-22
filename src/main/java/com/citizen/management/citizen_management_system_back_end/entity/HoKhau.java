package com.citizen.management.citizen_management_system_back_end.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ho_khau") // Theo chuẩn đặt tên của db
@Getter
@Setter
public class HoKhau {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ma_ho_khau")
    private String maHoKhau;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "ngay_dang_ky")
    @Temporal(TemporalType.DATE)
    private Date ngayDangKy;

    // Chủ hộ: FK đến NhanKhau (một nhân khẩu làm chủ hộ)
    @OneToOne
    @JoinColumn(name = "ma_nhan_khau_chu_ho") // FK tới nhân khẩu chủ hộ
    @JsonIgnoreProperties({"hoKhau", "danhSachTamTru", "danhSachTamVang", "taiKhoan", "hibernateLazyInitializer", "handler"})
    private NhanKhau chuHo;

    // Một HoKhau có nhiều NhanKhau (Thành viên hộ)
    // Giữ nguyên để API lấy chi tiết hộ khẩu vẫn thấy danh sách thành viên
    @OneToMany(mappedBy = "hoKhau", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NhanKhau> danhSachThanhVien = new ArrayList<>();


    public List<NhanKhau> getDanhSachThanhVien() {
        return new ArrayList<>(danhSachThanhVien);
    }
    public void addThanhVien(NhanKhau nk) {
        if (!danhSachThanhVien.contains(nk)) {
            danhSachThanhVien.add(nk);
            nk.setHoKhau(this);
        }
    }
    public void removeThanhVien(NhanKhau nk) {
        if (danhSachThanhVien.remove(nk)) {
            nk.setHoKhau(null);
        }
    }
    public void setDanhSachThanhVien(List<NhanKhau> newList) {
        for (NhanKhau nk : new ArrayList<>(danhSachThanhVien)) {
            removeThanhVien(nk);
        }
        if (newList != null) {
            for (NhanKhau nk : newList) {
                addThanhVien(nk);
            }
        }
    }
}