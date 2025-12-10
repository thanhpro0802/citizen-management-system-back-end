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
import java.util.LinkedHashMap;
import java.util.ArrayList;
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
    public Map<String, Object> thongKeHoKhau(String diaChi) {
        Map<String, Object> result = new HashMap<>();
        result.put("diaChi", diaChi);
        result.put("tongHoKhau", hoKhauService.getCountHoKhau(diaChi));
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
    public List<Map<String, Object>> thongKeTuoi() {
        Map<String, Long> raw = new LinkedHashMap<>();
        raw.put("0-17", 0L);
        raw.put("18-35", 0L);
        raw.put("36-60", 0L);
        raw.put("60+", 0L);

        LocalDate today = LocalDate.now();

        List<NhanKhau> list = nhanKhauRepository.findAll();
        for (NhanKhau nk : list) {
            LocalDate birthDate = nk.getNgaySinh().toLocalDate();
            int age = Period.between(birthDate, today).getYears();

            if (age <= 17)
                raw.put("0-17", raw.get("0-17") + 1);
            else if (age <= 35)
                raw.put("18-35", raw.get("18-35") + 1);
            else if (age <= 60)
                raw.put("36-60", raw.get("36-60") + 1);
            else
                raw.put("60+", raw.get("60+") + 1);
        }

        List<Map<String, Object>> listNivo = new ArrayList<>();

        raw.forEach((k, v) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", k);
            item.put("label", k);
            item.put("value", v);
            listNivo.add(item);
        });

        return listNivo;
    }

    @Override
    public List<Map<String, Object>> thongKeGioiTinh() {
        Map<String, Long> result = new HashMap<>();
        var list = thongKeRepository.theoGioiTinh();

        for (KeyValueProjection item : list) {
            result.put(item.getKey(), item.getValue());
        }

        List<Map<String, Object>> listNivo = new ArrayList<>();

        result.forEach((k, v) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", k);
            item.put("label", k);
            item.put("value", v);
            listNivo.add(item);
        });

        return listNivo;
    }

    @Override
    public List<Map<String, Object>> thongKeQueQuan() {
        Map<String, Long> result = new HashMap<>();
        var list = thongKeRepository.theoQueQuan();

        for (KeyValueProjection item : list) {
            result.put(item.getKey(), item.getValue());
        }

        List<Map<String, Object>> listNivo = new ArrayList<>();

        result.forEach((k, v) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", k);
            item.put("label", k);
            item.put("value", v);
            listNivo.add(item);
        });

        return listNivo;
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

    @Override
    public List<Map<String, Object>> thongKeSoNguoi() {
        Map<String, Long> raw = new LinkedHashMap<>();
        raw.put("1-2", 0L);
        raw.put("3-5", 0L);
        raw.put(">5", 0L);

        var list = thongKeRepository.theoSoNguoi();

        for (KeyValueProjection item : list) {
            Long num = item.getValue();

            if (num <= 2)
                raw.put("1-2", raw.get("1-2") + 1);
            else if (num <= 5)
                raw.put("3-5", raw.get("3-5") + 1);
            else
                raw.put(">5", raw.get(">5") + 1);
        }

        List<Map<String, Object>> listNivo = new ArrayList<>();

        raw.forEach((k, v) -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", k);
            item.put("label", k);
            item.put("value", v);
            listNivo.add(item);
        });

        return listNivo;
    }
}
