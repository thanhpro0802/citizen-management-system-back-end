package com.citizen.management.citizen_management_system_back_end.mapper;

import com.citizen.management.citizen_management_system_back_end.dto.NhanKhauDto;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;

public class NhanKhauMapper {
    public static NhanKhau mapToNhanKhau(NhanKhauDto nhanKhauDto) {
        return new NhanKhau(
                nhanKhauDto.getId(),
                nhanKhauDto.getHoTen(),
                nhanKhauDto.getGioiTinh()
        );
    }

    public static NhanKhauDto mapToNhanKhauDto(NhanKhau nhanKhau) {
        return new NhanKhauDto(
                nhanKhau.getId(),
                nhanKhau.getHoTen(),
                nhanKhau.getGioiTinh()
        );
    }
}
