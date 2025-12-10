package com.citizen.management.citizen_management_system_back_end.dto;

import java.util.List;

public class TachHoRequest {
    // ID chủ hộ mới (kiểu String UUID)
    private String maNhanKhauChuHoMoi;

    // Danh sách mã nhân khẩu tạo hộ mới (UUID)
    private List<String> maNhanKhauTachRa;

    // thông tin bổ sung
    private String diaChiMoi;
    private String ngayDangKyMoi; // Nếu muốn thông tin ngày đăng ký hộ mới

    public String getMaNhanKhauChuHoMoi() {
        return maNhanKhauChuHoMoi;
    }
    public void setMaNhanKhauChuHoMoi(String maNhanKhauChuHoMoi) {
        this.maNhanKhauChuHoMoi = maNhanKhauChuHoMoi;
    }

    public String getDiaChiMoi() {
        return diaChiMoi;
    }
    public void setDiaChiMoi(String diaChiMoi) {
        this.diaChiMoi = diaChiMoi;
    }

    public List<String> getMaNhanKhauTachRa() {
        return maNhanKhauTachRa;
    }
    public void setMaNhanKhauTachRa(List<String> maNhanKhauTachRa) {
        this.maNhanKhauTachRa = maNhanKhauTachRa;
    }

    public String getNgayDangKyMoi() {
        return ngayDangKyMoi;
    }
    public void setNgayDangKyMoi(String ngayDangKyMoi) {
        this.ngayDangKyMoi = ngayDangKyMoi;
    }
}