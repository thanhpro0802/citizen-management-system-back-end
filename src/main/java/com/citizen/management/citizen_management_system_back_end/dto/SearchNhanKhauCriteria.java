package com.citizen.management.citizen_management_system_back_end.dto;

public class SearchNhanKhauCriteria {
    private String q; // tên hoặc cccd
    private String gioiTinh;
    private String status;
    private Integer ageFrom;
    private Integer ageTo;
    private String maHoKhau;

    // Getters and Setters
    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAgeFrom() {
        return ageFrom;
    }

    public void setAgeFrom(Integer ageFrom) {
        this.ageFrom = ageFrom;
    }

    public Integer getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(Integer ageTo) {
        this.ageTo = ageTo;
    }

    public String getMaHoKhau() {
        return maHoKhau;
    }

    public void setMaHoKhau(String maHoKhau) {
        this.maHoKhau = maHoKhau;
    }
}
