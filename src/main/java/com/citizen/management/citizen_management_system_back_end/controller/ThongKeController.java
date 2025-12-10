package com.citizen.management.citizen_management_system_back_end.controller;

import lombok.AllArgsConstructor;
import com.citizen.management.citizen_management_system_back_end.service.ThongKeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/thong-ke")
public class ThongKeController {

    private final ThongKeService thongKeService;

    @GetMapping("/ho-khau")
    public ResponseEntity<Map<String, Object>> getThongKeHoKhau(
            @RequestParam(required = false) String phuong) {

        return ResponseEntity.ok(thongKeService.thongKeHoKhau(phuong));
    }

    @GetMapping("/do-tuoi")
    public ResponseEntity<List<Map<String, Object>>> getThongKeTuoi() {
        return ResponseEntity.ok(thongKeService.thongKeTuoi());
    }

    @GetMapping("/gioi-tinh")
    public ResponseEntity<List<Map<String, Object>>> getThongKeGioiTinh() {
        return ResponseEntity.ok(thongKeService.thongKeGioiTinh());
    }

    @GetMapping("/que-quan")
    public ResponseEntity<List<Map<String, Object>>> getThongKeQueQuan() {
        return ResponseEntity.ok(thongKeService.thongKeQueQuan());
    }

    @GetMapping("/dan-toc")
    public ResponseEntity<Map<String, Long>> getThongKeDanToc() {
        return ResponseEntity.ok(thongKeService.thongKeDanToc());
    }

    @GetMapping("/so-nguoi")
    public ResponseEntity<List<Map<String, Object>>> getThongKeSoNguoi() {
        return ResponseEntity.ok(thongKeService.thongKeSoNguoi());
    }
}
