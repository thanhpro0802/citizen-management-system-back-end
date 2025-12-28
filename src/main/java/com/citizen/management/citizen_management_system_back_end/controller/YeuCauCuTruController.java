package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.dto.request.YeuCauCuTruRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.XuLyYeuCauCuTruRequest;
import com.citizen.management.citizen_management_system_back_end.dto.response.MessageResponse;
import com.citizen.management.citizen_management_system_back_end.dto.response.ThongKeYeuCauResponse;
import com.citizen.management.citizen_management_system_back_end.dto.response.YeuCauCuTruResponse;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.enums.EnumLoaiYeuCauCuTru;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThaiYeuCau;
import com.citizen.management.citizen_management_system_back_end.repository.TaiKhoanRepository;
import com.citizen.management.citizen_management_system_back_end.security.services.UserDetailsImpl;
import com.citizen.management.citizen_management_system_back_end.service.YeuCauCuTruService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller cho quản lý yêu cầu cư trú
 */
@RestController
@RequestMapping("/api/yeu-cau-cu-tru")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class YeuCauCuTruController {

    private final YeuCauCuTruService yeuCauService;
    private final TaiKhoanRepository taiKhoanRepository;

    /**
     * Tạo yêu cầu cư trú mới (CONG_DAN)
     */
    @PostMapping
    //@PreAuthorize("hasAnyAuthority('CONG_DAN', 'CAN_BO')")
    public ResponseEntity<?> taoYeuCau(@Valid @RequestBody YeuCauCuTruRequest request) {
        try {
            TaiKhoan taiKhoan = getTaiKhoanHienTai();
            YeuCauCuTruResponse response = yeuCauService.taoYeuCau(request, taiKhoan);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Lấy danh sách yêu cầu của tôi (CONG_DAN)
     */
    @GetMapping("/cua-toi")
    //@PreAuthorize("hasAnyAuthority('CONG_DAN', 'CAN_BO')")
    public ResponseEntity<?> layYeuCauCuaToi() {
        try {
            TaiKhoan taiKhoan = getTaiKhoanHienTai();
            List<YeuCauCuTruResponse> yeuCauList = yeuCauService.layYeuCauCuaToi(taiKhoan);
            return ResponseEntity.ok(yeuCauList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Lấy chi tiết yêu cầu theo mã
     */
    @GetMapping("/{maYeuCau}")
    public ResponseEntity<?> layChiTiet(@PathVariable String maYeuCau) {
        try {
            TaiKhoan taiKhoan = getTaiKhoanHienTai();
            YeuCauCuTruResponse response = yeuCauService.layChiTietYeuCau(maYeuCau, taiKhoan);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Hủy yêu cầu (CONG_DAN - chỉ khi đang chờ xử lý)
     */
    @PutMapping("/{maYeuCau}/huy")
    //@PreAuthorize("hasAuthority('CONG_DAN')")
    public ResponseEntity<?> huyYeuCau(@PathVariable String maYeuCau) {
        try {
            TaiKhoan taiKhoan = getTaiKhoanHienTai();
            YeuCauCuTruResponse response = yeuCauService.huyYeuCau(maYeuCau, taiKhoan);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Lấy tất cả yêu cầu (CAN_BO, ADMIN)
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'CAN_BO_NHAN_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<?> layTatCaYeuCau(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<YeuCauCuTruResponse> yeuCauPage = yeuCauService.layTatCaYeuCau(pageable);
            return ResponseEntity.ok(yeuCauPage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Tìm kiếm yêu cầu theo tiêu chí (CAN_BO, ADMIN)
     */
    @GetMapping("/tim-kiem")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'CAN_BO_NHAN_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<?> timKiemYeuCau(
            @RequestParam(required = false) EnumTrangThaiYeuCau trangThai,
            @RequestParam(required = false) EnumLoaiYeuCauCuTru loaiYeuCau,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<YeuCauCuTruResponse> yeuCauPage = yeuCauService.timKiemYeuCau(trangThai, loaiYeuCau, pageable);
            return ResponseEntity.ok(yeuCauPage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Xử lý yêu cầu (CAN_BO, ADMIN)
     */
    @PutMapping("/{maYeuCau}/xu-ly")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'CAN_BO_NHAN_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<?> xuLyYeuCau(
            @PathVariable String maYeuCau,
            @Valid @RequestBody XuLyYeuCauCuTruRequest request) {
        try {
            TaiKhoan canBo = getTaiKhoanHienTai();
            YeuCauCuTruResponse response = yeuCauService.xuLyYeuCau(maYeuCau, request, canBo);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Phê duyệt yêu cầu (CAN_BO, ADMIN)
     */
    @PutMapping("/{maYeuCau}/phe-duyet")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'CAN_BO_NHAN_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<?> pheDuyetYeuCau(
            @PathVariable String maYeuCau,
            @RequestParam(required = false) String ghiChu) {
        try {
            TaiKhoan canBo = getTaiKhoanHienTai();
            YeuCauCuTruResponse response = yeuCauService.pheDuyetYeuCau(maYeuCau, ghiChu, canBo);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Nhận xử lý yêu cầu (CAN_BO, ADMIN) - Chuyển trạng thái từ CHO_XU_LY -> DANG_XU_LY
     */
    @PutMapping("/{maYeuCau}/nhan-xu-ly")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'CAN_BO_NHAN_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<?> nhanXuLyYeuCau(@PathVariable String maYeuCau) {
        try {
            TaiKhoan canBo = getTaiKhoanHienTai();
            YeuCauCuTruResponse response = yeuCauService.nhanXuLyYeuCau(maYeuCau, canBo);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Từ chối yêu cầu (CAN_BO, ADMIN)
     */
    @PutMapping("/{maYeuCau}/tu-choi")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'CAN_BO_NHAN_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<?> tuChoiYeuCau(
            @PathVariable String maYeuCau,
            @RequestParam String lyDoTuChoi) {
        try {
            TaiKhoan canBo = getTaiKhoanHienTai();
            YeuCauCuTruResponse response = yeuCauService.tuChoiYeuCau(maYeuCau, lyDoTuChoi, canBo);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Thống kê số lượng yêu cầu theo trạng thái (CAN_BO, ADMIN)
     */
    @GetMapping("/thong-ke")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'CAN_BO_NHAN_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<?> thongKeYeuCau() {
        try {
            long choXuLy = yeuCauService.demYeuCauTheoTrangThai(EnumTrangThaiYeuCau.CHO_XU_LY);
            long dangXuLy = yeuCauService.demYeuCauTheoTrangThai(EnumTrangThaiYeuCau.DANG_XU_LY);
            long daPheDuyet = yeuCauService.demYeuCauTheoTrangThai(EnumTrangThaiYeuCau.DA_PHE_DUYET);
            long tuChoi = yeuCauService.demYeuCauTheoTrangThai(EnumTrangThaiYeuCau.TU_CHOI);

            ThongKeYeuCauResponse response = new ThongKeYeuCauResponse(choXuLy, dangXuLy, daPheDuyet, tuChoi);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Đếm số yêu cầu chờ xử lý (CAN_BO, ADMIN) - Dùng cho badge sidebar
     */
    @GetMapping("/dem-cho-xu-ly")
    @PreAuthorize("hasAnyAuthority('CAN_BO_HO_KHAU', 'CAN_BO_NHAN_KHAU', 'ADMIN', 'TO_TRUONG', 'TO_PHO')")
    public ResponseEntity<?> demYeuCauChoXuLy() {
        try {
            long count = yeuCauService.demYeuCauTheoTrangThai(EnumTrangThaiYeuCau.CHO_XU_LY);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // ========== HELPER METHODS ==========

    private TaiKhoan getTaiKhoanHienTai() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Người dùng chưa đăng nhập!");
        }
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return taiKhoanRepository.findById(userDetails.getId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tài khoản"));
    }
}
