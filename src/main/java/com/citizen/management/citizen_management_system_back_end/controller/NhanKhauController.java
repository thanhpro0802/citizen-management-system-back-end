package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.dto.*;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.repository.TaiKhoanRepository;
import com.citizen.management.citizen_management_system_back_end.security.services.UserDetailsImpl;
import com.citizen.management.citizen_management_system_back_end.service.NhanKhauService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nhan-khau")
@RequiredArgsConstructor
public class NhanKhauController {

    private final NhanKhauService nhanKhauService;
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

    // === API MỚI CHO CÔNG DÂN: Xem thông tin nhân khẩu của bản thân và hộ khẩu ===
    /**
     * API lấy thông tin nhân khẩu của công dân đang đăng nhập
     * Trả về: Thông tin nhân khẩu của bản thân + danh sách thành viên cùng hộ khẩu
     */
    @GetMapping("/cua-toi")
    public ResponseEntity<?> layThongTinNhanKhauCuaToi() {
        TaiKhoan taiKhoan = getTaiKhoanHienTai();
        
        // Kiểm tra tài khoản có liên kết với nhân khẩu không
        if (taiKhoan.getNhanKhau() == null) {
            return ResponseEntity.ok().body(new java.util.HashMap<String, Object>() {{
                put("message", "Tài khoản chưa được liên kết với nhân khẩu");
                put("nhanKhau", null);
                put("thanhVienCungHo", new java.util.ArrayList<>());
            }});
        }
        
        // Lấy thông tin nhân khẩu và thành viên cùng hộ
        return ResponseEntity.ok(nhanKhauService.layThongTinNhanKhauVaHoKhau(taiKhoan.getNhanKhau().getMaNhanKhau()));
    }

    @PostMapping
    public ResponseEntity<NhanKhauDto> create(@Valid @RequestBody NhanKhauDto dto) {
        return ResponseEntity.ok(nhanKhauService.create(dto));
    }

    @GetMapping("/{ma}")
    public ResponseEntity<NhanKhauDto> get(@PathVariable("ma") String ma) {
        return ResponseEntity.ok(nhanKhauService.getById(ma));
    }

    @PutMapping("/{ma}")
    public ResponseEntity<NhanKhauDto> update(@PathVariable("ma") String ma, @Valid @RequestBody NhanKhauDto dto) {
        return ResponseEntity.ok(nhanKhauService.update(ma, dto));
    }

    @DeleteMapping("/{ma}")
    public ResponseEntity<Void> delete(@PathVariable("ma") String ma) {
        nhanKhauService.delete(ma);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<NhanKhauDto>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String gioiTinh,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer ageFrom,
            @RequestParam(required = false) Integer ageTo,
            @RequestParam(required = false) String maHoKhau,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "maNhanKhau") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        SearchNhanKhauCriteria criteria = new SearchNhanKhauCriteria();
        criteria.setQ(q);
        criteria.setGioiTinh(gioiTinh);
        criteria.setStatus(status);
        criteria.setAgeFrom(ageFrom);
        criteria.setAgeTo(ageTo);
        criteria.setMaHoKhau(maHoKhau);

        Sort sort = Sort.by(sortBy);
        sort = "asc".equalsIgnoreCase(sortDir) ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<NhanKhauDto> result = nhanKhauService.search(criteria, pageable);
        return ResponseEntity.ok(result);
    }

    // TamTru
    @PostMapping("/{ma}/tam-tru")
    public ResponseEntity<TamTruDto> registerTamTru(@PathVariable("ma") String ma, @Valid @RequestBody TamTruDto dto) {
        dto.setMaNhanKhau(ma);
        return ResponseEntity.ok(nhanKhauService.registerTamTru(dto));
    }

    // TamVang
    @PostMapping("/{ma}/tam-vang")
    public ResponseEntity<TamVangDto> registerTamVang(@PathVariable("ma") String ma, @Valid @RequestBody TamVangDto dto) {
        dto.setMaNhanKhau(ma);
        return ResponseEntity.ok(nhanKhauService.registerTamVang(dto));
    }

    // Khai tử
    @PostMapping("/{ma}/khai-tu")
    public ResponseEntity<NhanKhauDto> declareDeath(@PathVariable("ma") String ma) {
        return ResponseEntity.ok(nhanKhauService.declareDeath(ma));
    }
}
