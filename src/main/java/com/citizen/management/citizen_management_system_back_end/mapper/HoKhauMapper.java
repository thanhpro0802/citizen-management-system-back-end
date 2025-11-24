package com.citizen.management.citizen_management_system_back_end.mapper;

import com.citizen.management.citizen_management_system_back_end.dto.HoKhauDto;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;

public class HoKhauMapper {
    public static HoKhau mapToHoKhau(HoKhauDto hoKhauDto) {
        return new HoKhau(
                hoKhauDto.getId(),
                hoKhauDto.getPhuong()
        );
    }

    public static HoKhauDto mapToHoKhauDto(HoKhau hoKhau) {
        return new HoKhauDto(
                hoKhau.getId(),
                hoKhau.getPhuong()
        );
    }
}
