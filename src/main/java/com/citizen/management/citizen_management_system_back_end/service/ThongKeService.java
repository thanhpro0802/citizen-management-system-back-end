package com.citizen.management.citizen_management_system_back_end.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class ThongKeService {

    private final HoKhauService hoKhauService;
    private final NhanKhauService nhanKhauService;

    public Map<String, Object> thongKeHoKhau(String phuong) {
        Map<String, Object> result = new HashMap<>();
        result.put("phuong", phuong);
        result.put("tongHoKhau", hoKhauService.getCountHoKhau(phuong));
        return result;
    }

    public Map<String, Object> thongKeNhanKhau(String gioiTinh) {
        Map<String, Object> result = new HashMap<>();
        result.put("gioiTinh", gioiTinh);
        result.put("tongNhanKhau", nhanKhauService.getCountNhanKhau(gioiTinh));
        return result;
    }
}
