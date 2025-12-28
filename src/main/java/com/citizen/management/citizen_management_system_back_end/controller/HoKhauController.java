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

    // --- 1. API DÀNH CHO CÔNG DÂN (Xem của chính mình) ---
    @GetMapping("/cua-toi")
    // Không cần PreAuthorize vì SecurityConfig đã chặn .authenticated() rồi
    public ResponseEntity<HoKhau> xemHoKhauCuaToi() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cccd = authentication.getName();

        TaiKhoan taiKhoan = taiKhoanRepository.findByCccd(cccd)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        HoKhau hoKhau = hoKhauService.layHoKhauCuaToi(taiKhoan);

        if (hoKhau == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(hoKhau);
    }

    // --- 2. API QUẢN LÝ (Cần quyền Cán bộ, Tổ trưởng, Admin...) ---

    // Các biến quyền để tái sử dụng (giúp code gọn hơn nếu muốn, hoặc viết thẳng string)
    // Ở đây tôi viết thẳng string cho bạn dễ nhìn

    @GetMapping("/tim-kiem")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<List<HoKhau>> timKiemTheoDiaChi(@RequestParam String diaChi) {
        return ResponseEntity.ok(hoKhauService.timKiemTheoDiaChi(diaChi));
    }

    @GetMapping("/tim-kiem-chu-ho")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<List<HoKhau>> timKiemTheoChuHo(@RequestParam String keyword) {
        return ResponseEntity.ok(hoKhauService.timKiemTheoChuHo(keyword));
    }

    @GetMapping("/tim-kiem-tong-hop")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<List<HoKhau>> timKiemTongHop(@RequestParam String keyword) {
        return ResponseEntity.ok(hoKhauService.timKiemTongHop(keyword));
    }

    // [QUAN TRỌNG] API THÊM MỚI
    // 1. Sửa quyền thành CAN_BO_HO_KHAU
    // 2. Nhận HoKhauRequest (DTO) thay vì Entity
    @PostMapping
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<?> themMoi(@RequestBody HoKhauRequest request) {
        try {
            // Service đã được sửa để nhận Request DTO
            HoKhau ketQua = hoKhauService.taoMoi(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ketQua);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<?> updateHoKhau(@PathVariable String id, @RequestBody HoKhauRequest request) {
        try {
            HoKhau updatedHoKhau = hoKhauService.update(id, request);
            return ResponseEntity.ok(updatedHoKhau);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<Void> xoa(@PathVariable String id) {
        hoKhauService.xoa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<List<HoKhau>> xemDanhSach() {
        return ResponseEntity.ok(hoKhauService.layTatCa());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<HoKhau> xemChiTiet(@PathVariable String id) {
        HoKhau result = hoKhauService.layTheoId(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    // --- CÁC NGHIỆP VỤ NÂNG CAO ---

    @PostMapping("/{id}/tach-ho")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<HoKhau> tachHo(@PathVariable String id, @RequestBody TachHoRequest request) {
        return ResponseEntity.ok(hoKhauService.tachHo(id, request));
    }

    @PostMapping("/{id}/nhap-ho")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<HoKhau> nhapHo(@PathVariable String id, @RequestBody NhapHoRequest request) {
        return ResponseEntity.ok(hoKhauService.nhapHo(id, request));
    }

    @PutMapping("/{id}/doi-chu-ho")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<HoKhau> doiChuHo(@PathVariable String id, @RequestBody DoiChuHoRequest request) {
        return ResponseEntity.ok(hoKhauService.doiChuHo(id, request));
    }
}