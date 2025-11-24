package com.citizen.management.citizen_management_system_back_end.controller;

import lombok.AllArgsConstructor;
import com.citizen.management.citizen_management_system_back_end.service.HoKhauService;
import com.citizen.management.citizen_management_system_back_end.service.NhanKhauService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("api/ThongKe")

public class ThongKeController {

    private HoKhauService hoKhauService;
    private NhanKhauService nhanKhauService;

    // Build Count API
    @GetMapping("/HoKhau")
    public ResponseEntity<Map<String, Object>> getThongKeHoKhau(
            @RequestParam(required = false) String phuong) {
        Map<String, Object> result = new HashMap<>();
        result.put("phuong", phuong);
        result.put("tongHoKhau", hoKhauService.getCountHoKhau(phuong));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/NhanKhau")
    public ResponseEntity<Map<String, Object>> getThongKeNhanKhau(
            @RequestParam(required = false) String gioiTinh) {
        Map<String, Object> result = new HashMap<>();
        result.put("gioiTinh", gioiTinh);
        result.put("tongNhanKhau", nhanKhauService.getCountNhanKhau(gioiTinh));
        return ResponseEntity.ok(result);
    }
}
