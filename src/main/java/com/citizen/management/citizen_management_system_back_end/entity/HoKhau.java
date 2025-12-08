package com.citizen.management.citizen_management_system_back_end.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class HoKhau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String soHoKhau;
    private String diaChi;
    private String tenChuHo;
    private String soDienThoaiChuHo;

    // Quan hệ: Một hộ khẩu có nhiều nhân khẩu
    @OneToMany(mappedBy = "hoKhau", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NhanKhau> danhSachThanhVien;

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSoHoKhau() { return soHoKhau; }
    public void setSoHoKhau(String soHoKhau) { this.soHoKhau = soHoKhau; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getTenChuHo() { return tenChuHo; }
    public void setTenChuHo(String tenChuHo) { this.tenChuHo = tenChuHo; }

    public String getSoDienThoaiChuHo() { return soDienThoaiChuHo; }
    public void setSoDienThoaiChuHo(String soDienThoaiChuHo) { this.soDienThoaiChuHo = soDienThoaiChuHo; }

    public List<NhanKhau> getDanhSachThanhVien() { return danhSachThanhVien; }
    public void setDanhSachThanhVien(List<NhanKhau> danhSachThanhVien) {
        this.danhSachThanhVien = danhSachThanhVien;
        if (danhSachThanhVien != null) {
            for (NhanKhau nk : danhSachThanhVien) {
                nk.setHoKhau(this);
            }
        }
    }
}