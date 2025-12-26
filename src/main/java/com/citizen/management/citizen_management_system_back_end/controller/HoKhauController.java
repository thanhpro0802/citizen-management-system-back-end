package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.dto.request.DoiChuHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.NhapHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.TachHoRequest;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.service.HoKhauService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ho-khau")
@RequiredArgsConstructor
public class HoKhauController {

    private final HoKhauService hoKhauService;

    @PostMapping
    public ResponseEntity<HoKhau> themMoi(@RequestBody HoKhau hoKhau) {
        HoKhau ketQua = hoKhauService.taoMoi(hoKhau);
        return ResponseEntity.status(HttpStatus.CREATED).body(ketQua);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HoKhau> sua(@PathVariable String id, @RequestBody HoKhau hoKhau) {
        return ResponseEntity.ok(hoKhauService.capNhat(id, hoKhau));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> xoa(@PathVariable String id) {
        hoKhauService.xoa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<HoKhau>> xemDanhSach() {
        List<HoKhau> ds = hoKhauService.layTatCa();
        return ResponseEntity.ok(ds);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HoKhau> xemChiTiet(@PathVariable String id) {
        HoKhau result = hoKhauService.layTheoId(id);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/tach-ho")
    public ResponseEntity<HoKhau> tachHo(@PathVariable String id, @RequestBody TachHoRequest request) {
        return ResponseEntity.ok(hoKhauService.tachHo(id, request));
    }

    @PostMapping("/{id}/nhap-ho")
    public ResponseEntity<HoKhau> nhapHo(@PathVariable String id, @RequestBody NhapHoRequest request) {
        return ResponseEntity.ok(hoKhauService.nhapHo(id, request));
    }

    @PutMapping("/{id}/doi-chu-ho")
    public ResponseEntity<HoKhau> doiChuHo(@PathVariable String id, @RequestBody DoiChuHoRequest request) {
        return ResponseEntity.ok(hoKhauService.doiChuHo(id, request));
    }
}