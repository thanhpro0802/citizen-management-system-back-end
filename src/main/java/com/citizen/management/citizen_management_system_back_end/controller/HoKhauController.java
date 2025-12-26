package com.citizen.management.citizen_management_system_back_end.controller;


import com.citizen.management.citizen_management_system_back_end.dto.request.NhapHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.TachHoRequest;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.service.HoKhauService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/ho-khau")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class HoKhauController {

    private final HoKhauService hoKhauService;

    @PostMapping
    public ResponseEntity<HoKhau> themMoi(@RequestBody HoKhau hoKhau) {
        HoKhau ketQua = hoKhauService.taoMoi(hoKhau);
        return ResponseEntity.status(HttpStatus.CREATED).body(ketQua);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HoKhau> sua(@PathVariable String id, @RequestBody HoKhau hoKhau) {
        // Hàm này giờ đây xử lý cả việc đổi chủ hộ và cập nhật quan hệ thành viên
        return ResponseEntity.ok(hoKhauService.capNhat(id, hoKhau));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> xoa(@PathVariable String id) {
        hoKhauService.xoa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<HoKhau>> xemDanhSach() {
        return ResponseEntity.ok(hoKhauService.layTatCa());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HoKhau> xemChiTiet(@PathVariable String id) {
        HoKhau result = hoKhauService.layTheoId(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/tach-ho")
    public ResponseEntity<HoKhau> tachHo(@PathVariable String id, @RequestBody TachHoRequest request) {
        return ResponseEntity.ok(hoKhauService.tachHo(id, request));
    }

    @PostMapping("/{id}/nhap-ho")
    public ResponseEntity<HoKhau> nhapHo(@PathVariable String id, @RequestBody NhapHoRequest request) {
        return ResponseEntity.ok(hoKhauService.nhapHo(id, request));
    }

    @GetMapping("/cua-toi")
    public ResponseEntity<?> xemHoKhauCuaToi(Principal principal) {
        try {
            String username = principal.getName();
            HoKhau hoKhau = hoKhauService.xemHoKhauCuaToi(username);
            return ResponseEntity.ok(hoKhau);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}