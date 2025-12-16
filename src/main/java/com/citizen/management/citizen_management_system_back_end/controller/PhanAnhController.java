package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.dto.request.*;
import com.citizen.management.citizen_management_system_back_end.entity.LichSuPhanAnh;
import com.citizen.management.citizen_management_system_back_end.entity.PhanAnh;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.repository.TaiKhoanRepository;
import com.citizen.management.citizen_management_system_back_end.security.services.UserDetailsImpl;
import com.citizen.management.citizen_management_system_back_end.service.IPhanAnhService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/phan-anh")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // Cấu hình CORS cho React
public class PhanAnhController {
    private final IPhanAnhService phanAnhService;
    private final TaiKhoanRepository taiKhoanRepository;

    // --- Helper Method: Lấy User hiện tại từ Security Context ---
    private TaiKhoan getTaiKhoanHienTai() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Người dùng chưa đăng nhập!");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // userDetails.getId() trả về maTaiKhoan (UUID)
        return taiKhoanRepository.findById(userDetails.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tài khoản trong hệ thống"));
    }
    // -------------------------------------------------------------

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

    @PostMapping("/{id}/xu-ly-noi-bo")
    public ResponseEntity<Void> capNhatXuLyNoiBo(@PathVariable String id, @RequestBody XuLyNoiBoRequest request) {
        TaiKhoan canBoXuLy = getTaiKhoanHienTai();
        phanAnhService.capNhatXuLyNoiBo(id, request, canBoXuLy);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/phan-hoi")
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
    public ResponseEntity<List<PhanAnh>> layTatCa() {
        // API này dành cho Admin/Cán bộ xem toàn bộ danh sách
        return ResponseEntity.ok(phanAnhService.layTatCaPhanAnh());
    }
}
