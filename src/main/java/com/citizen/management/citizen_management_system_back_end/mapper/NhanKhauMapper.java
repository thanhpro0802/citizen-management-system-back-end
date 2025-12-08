package com.citizen.management.citizen_management_system_back_end.mapper;

import com.citizen.management.citizen_management_system_back_end.dto.NhanKhauDto;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;

public class NhanKhauMapper {
    public static NhanKhau mapToNhanKhau(NhanKhauDto nhanKhauDto) {
        return new NhanKhau(
                nhanKhauDto.getId(),
                nhanKhauDto.getHoTen(),
                nhanKhauDto.getNgaySinh(),
                nhanKhauDto.getGioiTinh(),
                nhanKhauDto.getQueQuan(),
                nhanKhauDto.getDanToc()
        );
    }

    public static NhanKhauDto mapToNhanKhauDto(NhanKhau nhanKhau) {
        return new NhanKhauDto(
                nhanKhau.getId(),
                nhanKhau.getHoTen(),
                nhanKhau.getNgaySinh(),
                nhanKhau.getGioiTinh(),
                nhanKhau.getQueQuan(),
                nhanKhau.getDanToc()
        );
    }
}
