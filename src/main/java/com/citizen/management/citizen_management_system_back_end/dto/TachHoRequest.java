package com.citizen.management.citizen_management_system_back_end.dto;

import java.util.List;

public class TachHoRequest {
    // Thay UUID bằng CCCD
    private String cccdChuHoMoi;
    private List<String> cccdNhanKhauTachRa;

    private String diaChiMoi;
    private String ngayDangKyMoi;

    public String getCccdChuHoMoi() {
        return cccdChuHoMoi;
    }
    public void setCccdChuHoMoi(String cccdChuHoMoi) {
        this.cccdChuHoMoi = cccdChuHoMoi;
    }

    public List<String> getCccdNhanKhauTachRa() {
        return cccdNhanKhauTachRa;
    }
    public void setCccdNhanKhauTachRa(List<String> cccdNhanKhauTachRa) {
        this.cccdNhanKhauTachRa = cccdNhanKhauTachRa;
    }

    public String getDiaChiMoi() {
        return diaChiMoi;
    }
    public void setDiaChiMoi(String diaChiMoi) {
        this.diaChiMoi = diaChiMoi;
    }

    public String getNgayDangKyMoi() {
        return ngayDangKyMoi;
    }
    public void setNgayDangKyMoi(String ngayDangKyMoi) {
        this.ngayDangKyMoi = ngayDangKyMoi;
    }
}