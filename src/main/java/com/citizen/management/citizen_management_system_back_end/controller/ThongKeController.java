package com.citizen.management.citizen_management_system_back_end.controller;

import lombok.AllArgsConstructor;
import com.citizen.management.citizen_management_system_back_end.service.ThongKeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/ThongKe")
public class ThongKeController {

    private final ThongKeService thongKeService;

    @GetMapping("/HoKhau")
    public ResponseEntity<Map<String, Object>> getThongKeHoKhau(
            @RequestParam(required = false) String phuong) {

        return ResponseEntity.ok(thongKeService.thongKeHoKhau(phuong));
    }

    @GetMapping("/NhanKhau")
    public ResponseEntity<Map<String, Object>> getThongKeNhanKhau(
            @RequestParam(required = false) String gioiTinh) {

        return ResponseEntity.ok(thongKeService.thongKeNhanKhau(gioiTinh));
    }

    @GetMapping("/Tuoi")
    public ResponseEntity<Map<String, Long>> getThongKeTuoi() {
        return ResponseEntity.ok(thongKeService.thongKeTuoi());
    }
    
    @GetMapping("/GioiTinh")
    public ResponseEntity<Map<String, Long>> getThongKeGioiTinh() {
        return ResponseEntity.ok(thongKeService.thongKeGioiTinh());
    }

    @GetMapping("/QueQuan")
    public ResponseEntity<Map<String, Long>> getThongKeQueQuan() {
        return ResponseEntity.ok(thongKeService.thongKeQueQuan());
    }

    @GetMapping("/DanToc")
    public ResponseEntity<Map<String, Long>> getThongKeDanToc() {
        return ResponseEntity.ok(thongKeService.thongKeDanToc());
    }
}
