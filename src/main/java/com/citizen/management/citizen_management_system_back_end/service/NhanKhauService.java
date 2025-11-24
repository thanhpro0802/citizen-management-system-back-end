package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.dto.NhanKhauDto;

import java.util.List;

public interface NhanKhauService {
    NhanKhauDto createNhanKhau(NhanKhauDto nhanKhauDto);

    NhanKhauDto getNhanKhauById(Long nhanKhauId);

    List<NhanKhauDto> getAllNhanKhau();

    NhanKhauDto updateNhanKhau(Long nhanKhauId, NhanKhauDto nhanKhauDto);

    void deleteNhanKhau(Long nhanKhauid);

    Long getCountNhanKhau(String gioiTinh);
}
