package com.citizen.management.citizen_management_system_back_end.dto;

import java.util.List;

public class NhapHoRequest {
    private String maHoNhapVao; // Vẫn giữ ID hộ khẩu đích
    private List<String> cccdNhanKhauNhapVao; // Thay list UUID bằng list CCCD
    private String quanHeVoiChuHo;

    public String getMaHoNhapVao() {
        return maHoNhapVao;
    }
    public void setMaHoNhapVao(String maHoNhapVao) {
        this.maHoNhapVao = maHoNhapVao;
    }

    public List<String> getCccdNhanKhauNhapVao() {
        return cccdNhanKhauNhapVao;
    }
    public void setCccdNhanKhauNhapVao(List<String> cccdNhanKhauNhapVao) {
        this.cccdNhanKhauNhapVao = cccdNhanKhauNhapVao;
    }

    public String getQuanHeVoiChuHo() {
        return quanHeVoiChuHo;
    }
    public void setQuanHeVoiChuHo(String quanHeVoiChuHo) {
        this.quanHeVoiChuHo = quanHeVoiChuHo;
    }
}