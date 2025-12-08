package com.citizen.management.citizen_management_system_back_end.dto;

import java.util.Collection;
import java.util.List;

public class NhapHoRequest {
    // ID hộ cần nhập (hộ đang vào)
    private Long idHoNhapVao;
    // Danh sách nhân khẩu nhập vào
    private List<Long> idNhanKhauNhapVao;

    public List<Long> getIdNhanKhauNhapVao() {
        return idNhanKhauNhapVao;
    }

    public Long getIdHoNhapVao() {
        return idHoNhapVao;
    }

    public void setIdHoNhapVao(Long idHoNhapVao) {
        this.idHoNhapVao = idHoNhapVao;
    }

    public void setIdNhanKhauNhapVao(List<Long> idNhanKhauNhapVao) {
        this.idNhanKhauNhapVao = idNhanKhauNhapVao;
    }
    // getters và setters
}