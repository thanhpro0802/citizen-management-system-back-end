package com.citizen.management.citizen_management_system_back_end.model;

import jakarta.persistence.*;

@Entity
public class NhanKhau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hoTen;
    private String cccd;
    private String gioiTinh;
    private String ngaySinh;
    private String quanHeVoiChuHo;

    @ManyToOne
    @JoinColumn(name = "hokhau_id")
    private HoKhau hoKhau;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getQuanHeVoiChuHo() { return quanHeVoiChuHo; }
    public void setQuanHeVoiChuHo(String quanHeVoiChuHo) { this.quanHeVoiChuHo = quanHeVoiChuHo; }

    public HoKhau getHoKhau() { return hoKhau; }
    public void setHoKhau(HoKhau hoKhau) { this.hoKhau = hoKhau; }
}