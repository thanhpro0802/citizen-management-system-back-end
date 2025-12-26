package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.dto.request.DoiChuHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.HoKhauRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.NhapHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.TachHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.response.MessageResponse;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.repository.TaiKhoanRepository;
import com.citizen.management.citizen_management_system_back_end.service.HoKhauService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ho-khau")
@RequiredArgsConstructor
public class HoKhauController {

    private final HoKhauService hoKhauService;
    private final TaiKhoanRepository taiKhoanRepository;

    @GetMapping("/cua-toi")
    public ResponseEntity<HoKhau> xemHoKhauCuaToi() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cccd = authentication.getName();

        TaiKhoan taiKhoan = taiKhoanRepository.findByCccd(cccd)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        HoKhau hoKhau = hoKhauService.layHoKhauCuaToi(taiKhoan);

        // THÊM: Nếu null thì trả về 404 Not Found
        if (hoKhau == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(hoKhau);
    }

    @GetMapping("/tim-kiem")
    @PreAuthorize("hasAnyAuthority('CAN_BO', 'ADMIN')")
    public ResponseEntity<List<HoKhau>> timKiemTheoDiaChi(@RequestParam String diaChi) {
        List<HoKhau> ketQua = hoKhauService.timKiemTheoDiaChi(diaChi);
        return ResponseEntity.ok(ketQua);
    }

    @GetMapping("/tim-kiem-chu-ho")
    @PreAuthorize("hasAnyAuthority('CAN_BO', 'ADMIN')")
    public ResponseEntity<List<HoKhau>> timKiemTheoChuHo(@RequestParam String keyword) {
        List<HoKhau> ketQua = hoKhauService.timKiemTheoChuHo(keyword);
        return ResponseEntity.ok(ketQua);
    }

    @GetMapping("/tim-kiem-tong-hop")
    @PreAuthorize("hasAnyAuthority('CAN_BO', 'ADMIN')")
    public ResponseEntity<List<HoKhau>> timKiemTongHop(@RequestParam String keyword) {
        return ResponseEntity.ok(hoKhauService.timKiemTongHop(keyword));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CAN_BO', 'ADMIN')")
    public ResponseEntity<HoKhau> themMoi(@RequestBody HoKhau hoKhau) {
        HoKhau ketQua = hoKhauService.taoMoi(hoKhau);
        return ResponseEntity.status(HttpStatus.CREATED).body(ketQua);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CAN_BO', 'ADMIN')")
    public ResponseEntity<?> updateHoKhau(@PathVariable String id, @RequestBody HoKhauRequest request) {
        try {
            // Gọi hàm update mới viết ở Service
            HoKhau updatedHoKhau = hoKhauService.update(id, request);
            return ResponseEntity.ok(updatedHoKhau);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CAN_BO', 'ADMIN')")
    public ResponseEntity<Void> xoa(@PathVariable String id) {
        hoKhauService.xoa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('CAN_BO', 'ADMIN')")
    public ResponseEntity<List<HoKhau>> xemDanhSach() {
        List<HoKhau> ds = hoKhauService.layTatCa();
        return ResponseEntity.ok(ds);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CAN_BO', 'ADMIN')")
    public ResponseEntity<HoKhau> xemChiTiet(@PathVariable String id) {
        HoKhau result = hoKhauService.layTheoId(id);
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/tach-ho")
    @PreAuthorize("hasAnyAuthority('CAN_BO', 'ADMIN')")
    public ResponseEntity<HoKhau> tachHo(@PathVariable String id, @RequestBody TachHoRequest request) {
        return ResponseEntity.ok(hoKhauService.tachHo(id, request));
    }

    @PostMapping("/{id}/nhap-ho")
    @PreAuthorize("hasAnyAuthority('CAN_BO', 'ADMIN')")
    public ResponseEntity<HoKhau> nhapHo(@PathVariable String id, @RequestBody NhapHoRequest request) {
        return ResponseEntity.ok(hoKhauService.nhapHo(id, request));
    }

    @PutMapping("/{id}/doi-chu-ho")
    @PreAuthorize("hasAnyAuthority('CAN_BO', 'ADMIN')")
    public ResponseEntity<HoKhau> doiChuHo(@PathVariable String id, @RequestBody DoiChuHoRequest request) {
        return ResponseEntity.ok(hoKhauService.doiChuHo(id, request));
    }
}