package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.dto.request.*;
import com.citizen.management.citizen_management_system_back_end.entity.LichSuPhanAnh;
import com.citizen.management.citizen_management_system_back_end.entity.PhanAnh;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.enums.EnumMucDoKhanCap;
import com.citizen.management.citizen_management_system_back_end.repository.TaiKhoanRepository;
import com.citizen.management.citizen_management_system_back_end.security.services.UserDetailsImpl;
import com.citizen.management.citizen_management_system_back_end.service.IPhanAnhService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException; // Import lỗi 403
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // Import Map để hứng JSON

@RestController
@RequestMapping("/api/v1/phan-anh")
@RequiredArgsConstructor
public class PhanAnhController {
    private final IPhanAnhService phanAnhService;
    private final TaiKhoanRepository taiKhoanRepository;

    // --- Helper Method: Lấy User hiện tại ---
    private TaiKhoan getTaiKhoanHienTai() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Người dùng chưa đăng nhập!");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return taiKhoanRepository.findById(userDetails.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tài khoản"));
    }

    @PostMapping
    public ResponseEntity<PhanAnh> guiPhanAnhMoi(@RequestBody GuiPhanAnhRequest request) {
        TaiKhoan nguoiGui = getTaiKhoanHienTai();
        PhanAnh paMoi = phanAnhService.guiPhanAnh(request, nguoiGui);
        return new ResponseEntity<>(paMoi, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/phan-cong")
    public ResponseEntity<PhanAnh> phanCongXuLy(@PathVariable String id, @RequestBody PhanCongRequest request) {
        TaiKhoan nguoiPhanCong = getTaiKhoanHienTai();
        PhanAnh paCapNhat = phanAnhService.phanCongXuLy(id, request, nguoiPhanCong);
        return ResponseEntity.ok(paCapNhat);
    }

    // --- SỬA 1: Đổi thành PUT và đường dẫn ngắn gọn để khớp Frontend ---
    @PutMapping("/{id}/xu-ly")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAN_BO_PHAN_ANH', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<Void> capNhatXuLyNoiBo(@PathVariable String id, @RequestBody XuLyNoiBoRequest request) {
        TaiKhoan canBoXuLy = getTaiKhoanHienTai();
        phanAnhService.capNhatXuLyNoiBo(id, request, canBoXuLy);
        return ResponseEntity.ok().build();
    }

    // --- SỬA 2: Đổi thành PUT cho đồng bộ ---
    @PutMapping("/{id}/phan-hoi")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAN_BO_PHAN_ANH', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<PhanAnh> phanHoiCongDan(@PathVariable String id, @RequestBody PhanHoiRequest request) {
        TaiKhoan canBoPhanHoi = getTaiKhoanHienTai();
        PhanAnh paCapNhat = phanAnhService.phanHoiCongDan(id, request, canBoPhanHoi);
        return ResponseEntity.ok(paCapNhat);
    }

    @PutMapping("/{id}/danh-gia")
    public ResponseEntity<PhanAnh> danhGiaPhanHoi(@PathVariable String id, @RequestBody DanhGiaRequest request) {
        TaiKhoan nguoiDanhGia = getTaiKhoanHienTai();
        PhanAnh paCapNhat = phanAnhService.danhGiaPhanHoi(id, request, nguoiDanhGia);
        return ResponseEntity.ok(paCapNhat);
    }

    // --- SỬA 3: QUAN TRỌNG NHẤT (Fix lỗi cập nhật mức độ) ---
    @PutMapping("/{id}/muc-do-khan-cap")
    public ResponseEntity<?> capNhatMucDoKhanCap(@PathVariable String id, @RequestBody Map<String, String> body) {
        try {
            // 1. Lấy dữ liệu từ JSON { "mucDo": "THAP" }
            String mucDoStr = body.get("mucDo");
            if (mucDoStr == null) {
                return ResponseEntity.badRequest().body("Vui lòng chọn mức độ!");
            }

            // 2. Convert String sang Enum
            EnumMucDoKhanCap mucDo = EnumMucDoKhanCap.valueOf(mucDoStr);

            // 3. Gọi Service (Service sẽ tự check quyền và throw AccessDeniedException nếu sai)
            PhanAnh pa = phanAnhService.capNhatMucDoKhanCap(id, mucDo);

            return ResponseEntity.ok(pa);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Mức độ không hợp lệ: " + body.get("mucDo"));
        } catch (AccessDeniedException e) {
            // Trả về đúng mã 403 để Frontend hiển thị thông báo "Lỗi quyền"
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // --- Các API Get giữ nguyên ---
    @GetMapping("/cua-toi")
    public ResponseEntity<List<PhanAnh>> layDanhSachCuaToi() {
        TaiKhoan nguoiGui = getTaiKhoanHienTai();
        List<PhanAnh> danhSach = phanAnhService.layDanhSachPhanAnhCuaToi(nguoiGui);
        return ResponseEntity.ok(danhSach);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhanAnh> layChiTiet(@PathVariable String id) {
        return ResponseEntity.ok(phanAnhService.layChiTietPhanAnh(id));
    }

    @GetMapping("/{id}/lich-su")
    public ResponseEntity<List<LichSuPhanAnh>> layLichSu(@PathVariable String id) {
        return ResponseEntity.ok(phanAnhService.layLichSuPhanAnh(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAN_BO_PHAN_ANH', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<List<PhanAnh>> layTatCa() {
        return ResponseEntity.ok(phanAnhService.layTatCaPhanAnh());
    }
}