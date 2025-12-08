package com.citizen.management.citizen_management_system_back_end.service.impl;

import lombok.AllArgsConstructor;

import com.citizen.management.citizen_management_system_back_end.dto.projection.KeyValueProjection;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.repository.NhanKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.ThongKeRepository;
import com.citizen.management.citizen_management_system_back_end.service.HoKhauService;
import com.citizen.management.citizen_management_system_back_end.service.NhanKhauService;
import com.citizen.management.citizen_management_system_back_end.service.ThongKeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


@Service
@AllArgsConstructor

public class ThongKeServiceImpl implements ThongKeService {
    private final HoKhauService hoKhauService;
    private final NhanKhauService nhanKhauService; 
    private final NhanKhauRepository nhanKhauRepository;
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
    public Map<String, Long> thongKeTuoi() {
        List<NhanKhau> list = nhanKhauRepository.findAll();

        Map<String, Long> result = new HashMap<>();

        result.put("0-17", 0L);
        result.put("18-35", 0L);
        result.put("36-60", 0L);
        result.put("60+", 0L);

        LocalDate today = LocalDate.now();

        for (NhanKhau nk : list) {

            LocalDate birthDate = nk.getNgaySinh().toLocalDate();

            int age = Period.between(birthDate, today).getYears();

            if (age <= 17) result.put("0-17", result.get("0-17") + 1);
            else if (age <= 35) result.put("18-35", result.get("18-35") + 1);
            else if (age <= 60) result.put("36-60", result.get("36-60") + 1);
            else result.put("60+", result.get("60+") + 1);
        }

        return result;
    }

    @Override
    public Map<String, Long> thongKeGioiTinh() {
        Map<String, Long> result = new HashMap<>();
        var list = thongKeRepository.theoGioiTinh();

        for (KeyValueProjection item : list) {
            result.put(item.getKey(), item.getValue());
        }

        return result;
    }

    @Override
    public Map<String, Long> thongKeQueQuan() {
        Map<String, Long> result = new HashMap<>();
        var list = thongKeRepository.theoQueQuan();

        for (KeyValueProjection item : list) {
            result.put(item.getKey(), item.getValue());
        }

        return result;
    }

    @Override
    public Map<String, Long> thongKeDanToc() {
        Map<String, Long> result = new HashMap<>();
        var list = thongKeRepository.theoDanToc();

        for (KeyValueProjection item : list) {
            result.put(item.getKey(), item.getValue());
        }

        return result;
    }
}
