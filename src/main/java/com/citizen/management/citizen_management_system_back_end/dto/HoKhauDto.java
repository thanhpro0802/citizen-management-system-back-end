package com.citizen.management.citizen_management_system_back_end.dto;

import java.util.Date;
import java.util.List;

public class HoKhauDto {
    private String maHoKhau;
    private String diaChi;
    private Date ngayDangKy;
    private NhanKhauDto chuHo;
    private List<NhanKhauDto> danhSachThanhVien;

    // Getters and Setters
    public String getMaHoKhau() {
        return maHoKhau;
    }

    public void setMaHoKhau(String maHoKhau) {
        this.maHoKhau = maHoKhau;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public Date getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(Date ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public NhanKhauDto getChuHo() {
        return chuHo;
    }

    public void setChuHo(NhanKhauDto chuHo) {
        this.chuHo = chuHo;
    }

    public List<NhanKhauDto> getDanhSachThanhVien() {
        return danhSachThanhVien;
    }

    public void setDanhSachThanhVien(List<NhanKhauDto> danhSachThanhVien) {
        this.danhSachThanhVien = danhSachThanhVien;
    }
}
