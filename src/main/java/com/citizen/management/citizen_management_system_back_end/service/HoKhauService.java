package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.dto.HoKhauDto;

import java.util.List;

public interface HoKhauService {
    HoKhauDto createHoKhau(HoKhauDto hoKhauDto);

    HoKhauDto getHoKhauById(Long hoKhauId);

    List<HoKhauDto> getAllHoKhau();

    HoKhauDto updateHoKhau(Long hoKhauId, HoKhauDto hoKhauDto);

    void deleteHoKhauById(Long hoKhauId);

    Long getCountHoKhau(String diaChi);
}
