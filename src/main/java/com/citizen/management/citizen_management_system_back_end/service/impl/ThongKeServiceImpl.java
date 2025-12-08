package com.citizen.management.citizen_management_system_back_end.service.impl;

import lombok.AllArgsConstructor;

import com.citizen.management.citizen_management_system_back_end.dto.projection.KeyValueProjection;
import com.citizen.management.citizen_management_system_back_end.repository.ThongKeRepository;
import com.citizen.management.citizen_management_system_back_end.service.HoKhauService;
import com.citizen.management.citizen_management_system_back_end.service.NhanKhauService;
import com.citizen.management.citizen_management_system_back_end.service.ThongKeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor

public class ThongKeServiceImpl implements ThongKeService {
    private final HoKhauService hoKhauService;
    private final NhanKhauService nhanKhauService; 
    private final ThongKeRepository thongKeRepository;

    @Override
    public Map<String, Object> thongKeHoKhau(String phuong) {
        Map<String, Object> result = new HashMap<>();
        result.put("phuong", phuong);
        result.put("tongHoKhau", hoKhauService.getCountHoKhau(phuong));
        return result;
    }

    @Override
    public Map<String, Object> thongKeNhanKhau(String gioiTinh) {
        Map<String, Object> result = new HashMap<>();
        result.put("gioiTinh", gioiTinh);
        result.put("tongNhanKhau", nhanKhauService.getCountNhanKhau(gioiTinh));
        return result;
    }

    @Override
    public Map<String, Long> thongKeGioiTinh() {
        Map<String, Long> result = new HashMap<>();
        var list = thongKeRepository.theoGioiTinh();

        for (KeyValueProjection item : list) {
            result.put(item.getGioiTinh(), item.getSoLuong());
        }

        return result;
    }
}
