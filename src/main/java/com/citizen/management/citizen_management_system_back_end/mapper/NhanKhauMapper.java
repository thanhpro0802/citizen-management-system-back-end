package com.citizen.management.citizen_management_system_back_end.mapper;

import com.citizen.management.citizen_management_system_back_end.dto.NhanKhauDto;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;

public class NhanKhauMapper {

    public static NhanKhau mapToNhanKhau(NhanKhauDto dto) {

        HoKhau hoKhau = null;
        if (dto.getIdHoKhau() != null) {
            hoKhau = new HoKhau();
            hoKhau.setId(dto.getIdHoKhau());
        }

        return new NhanKhau(
                dto.getId(),
                hoKhau,
                dto.getHoTen(),
                dto.getNgaySinh(),
                dto.getGioiTinh(),
                dto.getQueQuan(),
                dto.getDanToc());
    }

    public static NhanKhauDto mapToNhanKhauDto(NhanKhau nk) {

        Long idHoKhau = null;
        if (nk.getHoKhau() != null) {
            idHoKhau = nk.getHoKhau().getId();
        }

        return new NhanKhauDto(
                nk.getId(),
                idHoKhau,
                nk.getHoTen(),
                nk.getNgaySinh(),
                nk.getGioiTinh(),
                nk.getQueQuan(),
                nk.getDanToc());
    }
}
