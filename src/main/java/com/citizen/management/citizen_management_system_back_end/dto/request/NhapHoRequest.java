package com.citizen.management.citizen_management_system_back_end.dto.request;

import java.util.List;

public class NhapHoRequest {
    // Mã hộ nhập (hộ đang vào) - kiểu String UUID
    private String maHoNhapVao;

    // Danh sách nhân khẩu nhập vào - kiểu String UUID
    private List<String> maNhanKhauNhapVao;

    // Getters và setters
    public String getMaHoNhapVao() {
        return maHoNhapVao;
    }
    public void setMaHoNhapVao(String maHoNhapVao) {
        this.maHoNhapVao = maHoNhapVao;
    }

    public List<String> getMaNhanKhauNhapVao() {
        return maNhanKhauNhapVao;
    }
    public void setMaNhanKhauNhapVao(List<String> maNhanKhauNhapVao) {
        this.maNhanKhauNhapVao = maNhanKhauNhapVao;
    }
}