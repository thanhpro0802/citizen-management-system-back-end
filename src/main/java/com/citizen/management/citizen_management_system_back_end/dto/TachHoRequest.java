package com.citizen.management.citizen_management_system_back_end.dto;

import com.citizen.management.citizen_management_system_back_end.model.NhanKhau;

import java.util.List;

public class TachHoRequest {
    // ID chủ hộ mới
    private Long idChuHoMoi;
    // Danh sách nhân khẩu tạo thành hộ mới (ID hoặc thông tin chi tiết)
    private List<Long> idNhanKhauTachRa;
    // thông tin bổ sung (địa chỉ hộ mới, ...)
    private String diaChiMoi;

    public Long getIdChuHoMoi() {
        return idChuHoMoi;
    }

    public String getDiaChiMoi() {
        return diaChiMoi;
    }

    public List<Long> getIdNhanKhauTachRa() {
        return idNhanKhauTachRa;
    }
}
