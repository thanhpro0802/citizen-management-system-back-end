package com.citizen.management.citizen_management_system_back_end.service.impl;

import lombok.AllArgsConstructor;
import com.citizen.management.citizen_management_system_back_end.dto.HoKhauDto;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.exception.ResourceNotFoundException;
import com.citizen.management.citizen_management_system_back_end.mapper.HoKhauMapper;
import com.citizen.management.citizen_management_system_back_end.repository.HoKhauRepository;
import com.citizen.management.citizen_management_system_back_end.service.HoKhauService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service

public class HoKhauServiceImpl implements HoKhauService {

    private HoKhauRepository hoKhauRepository;

    @Override
    public HoKhauDto createHoKhau(HoKhauDto hoKhauDto) {
        HoKhau hoKhau = HoKhauMapper.mapToHoKhau(hoKhauDto);
        HoKhau savedHoKhau = hoKhauRepository.save(hoKhau);
        return HoKhauMapper.mapToHoKhauDto(savedHoKhau);
    }

    @Override
    public HoKhauDto getHoKhauById(Long hoKhauId) {
        HoKhau hoKhau = hoKhauRepository.findById(hoKhauId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong ton tai ho khau!"));
        return HoKhauMapper.mapToHoKhauDto(hoKhau);
    }

    @Override
    public List<HoKhauDto> getAllHoKhau() {
        List<HoKhau> listHoKhau = hoKhauRepository.findAll();
        return listHoKhau.stream().map(HoKhauMapper::mapToHoKhauDto)
                .collect(Collectors.toList());
    }

    @Override
    public HoKhauDto updateHoKhau(Long hoKhauId, HoKhauDto hoKhauDto) {
        HoKhau hoKhau = hoKhauRepository.findById(hoKhauId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong ton tai ho khau!"));

        hoKhau.setPhuong(hoKhauDto.getPhuong());
        HoKhau updatedHoKhau = hoKhauRepository.save(hoKhau);
        return HoKhauMapper.mapToHoKhauDto(updatedHoKhau);
    }

    @Override
    public void deleteHoKhauById(Long hoKhauId) {
        hoKhauRepository.deleteById(hoKhauId);
    }

    @Override
    public Long getCountHoKhau(String phuong) {
        if (phuong != null)
            return hoKhauRepository.countByPhuong(phuong);
        return hoKhauRepository.count();
    }
}
