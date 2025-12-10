package com.citizen.management.citizen_management_system_back_end.mapper;

import com.citizen.management.citizen_management_system_back_end.dto.HoKhauDto;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;

public class HoKhauMapper {

    public static HoKhau mapToHoKhau(HoKhauDto dto, NhanKhau chuHo) {
        HoKhau hk = new HoKhau();
        hk.setId(dto.getId());
        hk.setChuHo(chuHo);
        hk.setDiaChi(dto.getDiaChi());
        return hk;
    }

    public static HoKhauDto mapToHoKhauDto(HoKhau hk) {
        HoKhauDto dto = new HoKhauDto();
        dto.setId(hk.getId());
        dto.setIdChuHo(hk.getChuHo().getId());
        dto.setDiaChi(hk.getDiaChi());
        return dto;
    }
}
