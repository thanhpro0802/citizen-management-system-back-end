package com.citizen.management.citizen_management_system_back_end.service.impl;

import lombok.AllArgsConstructor;
import com.citizen.management.citizen_management_system_back_end.dto.NhanKhauDto;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.exception.ResourceNotFoundException;
import com.citizen.management.citizen_management_system_back_end.mapper.NhanKhauMapper;
import com.citizen.management.citizen_management_system_back_end.repository.HoKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.NhanKhauRepository;
import com.citizen.management.citizen_management_system_back_end.service.NhanKhauService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service

public class NhanKhauServiceImpl implements NhanKhauService {

    private HoKhauRepository hoKhauRepository;
    private NhanKhauRepository nhanKhauRepository;

    @Override
    public NhanKhauDto createNhanKhau(NhanKhauDto nhanKhauDto) {
        NhanKhau nhanKhau = NhanKhauMapper.mapToNhanKhau(nhanKhauDto);
        NhanKhau savedNhanKhau = nhanKhauRepository.save(nhanKhau);
        return NhanKhauMapper.mapToNhanKhauDto(savedNhanKhau);
    }

    @Override
    public NhanKhauDto getNhanKhauById(Long nhanKhauId) {
        NhanKhau nhanKhau = nhanKhauRepository.findById(nhanKhauId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong ton tai nhan khau!"));
        return NhanKhauMapper.mapToNhanKhauDto(nhanKhau);
    }

    @Override
    public List<NhanKhauDto> getAllNhanKhau() {
        List<NhanKhau> listNhanKhau = nhanKhauRepository.findAll();
        return listNhanKhau.stream().map(NhanKhauMapper::mapToNhanKhauDto)
                .collect(Collectors.toList());
    }

    @Override
    public NhanKhauDto updateNhanKhau(Long nhanKhauId, NhanKhauDto nhanKhauDto) {
        NhanKhau nhanKhau = nhanKhauRepository.findById(nhanKhauId)
                .orElseThrow(() -> new ResourceNotFoundException("Khong ton tai nhan khau!"));

        HoKhau hoKhau = hoKhauRepository.findById(nhanKhauDto.getIdHoKhau())
                .orElseThrow(() -> new ResourceNotFoundException("Khong ton tai ho khau!"));

        nhanKhau.setHoKhau(hoKhau);
        nhanKhau.setHoTen(nhanKhauDto.getHoTen());
        nhanKhau.setNgaySinh(nhanKhauDto.getNgaySinh());
        nhanKhau.setGioiTinh(nhanKhauDto.getGioiTinh());
        nhanKhau.setQueQuan(nhanKhauDto.getQueQuan());
        nhanKhau.setDanToc(nhanKhauDto.getDanToc());

        NhanKhau updatedNhanKhau = nhanKhauRepository.save(nhanKhau);
        return NhanKhauMapper.mapToNhanKhauDto(updatedNhanKhau);
    }

    @Override
    public void deleteNhanKhau(Long nhanKhauId) {
        nhanKhauRepository.deleteById(nhanKhauId);
    }

    @Override
    public Long getCountNhanKhau(String gioiTinh) {
        if (gioiTinh != null)
            return nhanKhauRepository.countByGioiTinh(gioiTinh);
        return nhanKhauRepository.count();
    }
}
