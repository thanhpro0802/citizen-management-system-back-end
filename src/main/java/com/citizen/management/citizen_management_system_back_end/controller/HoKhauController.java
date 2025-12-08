package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.dto.DoiChuHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.NhapHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.TachHoRequest;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.service.HoKhauService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; // Import thêm
import org.springframework.http.ResponseEntity; // Import thêm
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ho-khau")
@RequiredArgsConstructor
public class HoKhauController {
    
    private final HoKhauService hoKhauService;

    // 1. Thêm mới: Trả về 201 Created
    @PostMapping
    public ResponseEntity<HoKhau> themMoi(@RequestBody HoKhau hoKhau) {
        HoKhau ketQua = hoKhauService.taoMoi(hoKhau);
        return ResponseEntity.status(HttpStatus.CREATED).body(ketQua);
    }

    // 2. Cập nhật: Trả về 200 OK
    @PutMapping("/{id}")
    public ResponseEntity<HoKhau> sua(@PathVariable Long id, @RequestBody HoKhau hoKhau) {
        return ResponseEntity.ok(hoKhauService.capNhat(id, hoKhau));
    }

    // 3. Xóa: Trả về 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> xoa(@PathVariable Long id) {
        hoKhauService.xoa(id);
        return ResponseEntity.noContent().build();
    }

    // 4. Xem danh sách: Trả về 200 OK
    @GetMapping
    public ResponseEntity<List<HoKhau>> xemDanhSach() {
        return ResponseEntity.ok(hoKhauService.layTatCa());
    }

    // 5. Xem chi tiết: Trả về 200 OK 
    // (Lưu ý: Nếu service trả về Optional thì dùng .map(...).orElse(...), nếu trả về object thì dùng .ok() và để ExceptionHandler lo phần lỗi 404)
    @GetMapping("/{id}")
    public ResponseEntity<HoKhau> xemChiTiet(@PathVariable Long id) {
        return ResponseEntity.ok(hoKhauService.layTheoId(id));
    }

    // --- Các nghiệp vụ đặc thù (Tách, Nhập, Đổi chủ hộ) ---

    @PostMapping("/{id}/tach-ho")
    public ResponseEntity<HoKhau> tachHo(@PathVariable Long id, @RequestBody TachHoRequest request) {
        return ResponseEntity.ok(hoKhauService.tachHo(id, request));
    }

    @PostMapping("/{id}/nhap-ho")
    public ResponseEntity<HoKhau> nhapHo(@PathVariable Long id, @RequestBody NhapHoRequest request) {
        return ResponseEntity.ok(hoKhauService.nhapHo(id, request));
    }

    @PutMapping("/{id}/doi-chu-ho")
    public ResponseEntity<HoKhau> doiChuHo(@PathVariable Long id, @RequestBody DoiChuHoRequest request) {
        return ResponseEntity.ok(hoKhauService.doiChuHo(id, request));
    }
}
