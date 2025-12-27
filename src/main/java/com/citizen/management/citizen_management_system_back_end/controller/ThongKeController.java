package com.citizen.management.citizen_management_system_back_end.controller;

import lombok.AllArgsConstructor;

import com.citizen.management.citizen_management_system_back_end.enums.EnumThongKe;
import com.citizen.management.citizen_management_system_back_end.service.ThongKeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("/api/thong-ke")

public class ThongKeController {

    private final ThongKeService thongKeService;

    @GetMapping("/nhan-khau")
    public ResponseEntity<Map<String, Object>> thongKeNhanKhau(
            @RequestParam List<EnumThongKe> types) {

        return ResponseEntity.ok(thongKeService.thongKeNhanKhau(types));
    }

    @GetMapping("/ho-khau")
    public ResponseEntity<Map<String, Object>> thongKeHoKhau(
            @RequestParam List<EnumThongKe> types) {

        return ResponseEntity.ok(thongKeService.thongKeHoKhau(types));
    }

    @GetMapping("/phan-anh")
    public ResponseEntity<Map<String, Object>> thongKePhanAnh(
            @RequestParam List<EnumThongKe> types,
            @RequestParam String startDate) {
        return ResponseEntity.ok(thongKeService.thongKePhanAnh(types, LocalDate.parse(startDate)));
    }

    @GetMapping("/phan-anh/nam")
    public ResponseEntity<Map<String, Object>> thongKePhanAnhTheoNam(
            @RequestParam int year) {
        return ResponseEntity.ok(thongKeService.thongKePhanAnhTheoNam(year));
    }

    @GetMapping("/tam-tru-tam-vang")
    public ResponseEntity<Map<String, Object>> thongKeTamTruTamVang(
            @RequestParam List<EnumThongKe> types,
            @RequestParam String startDate) {
        return ResponseEntity.ok(thongKeService.thongKeTamTruTamVang(types, LocalDate.parse(startDate)));
    }

    @GetMapping("/tam-tru-tam-vang/tuan")
    public ResponseEntity<Map<String, Object>> thongKeTamTruTamVangTheoTuan(
            @RequestParam List<EnumThongKe> types,
            @RequestParam String startDate) {
        return ResponseEntity.ok(thongKeService.thongKeTamTruTamVangTheoTuan(types, LocalDate.parse(startDate)));
    }
}