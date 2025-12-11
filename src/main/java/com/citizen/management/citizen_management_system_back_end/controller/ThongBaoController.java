package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.entity.ThongBao;
import com.citizen.management.citizen_management_system_back_end.repository.TaiKhoanRepository;
import com.citizen.management.citizen_management_system_back_end.repository.ThongBaoRepository;
import com.citizen.management.citizen_management_system_back_end.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/thong-bao")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ThongBaoController {
    private final ThongBaoRepository thongBaoRepository;
    private final TaiKhoanRepository taiKhoanRepository;

    @GetMapping("/cua-toi")
    public ResponseEntity<List<ThongBao>> layThongBaoCuaToi() {
        // Lấy thông tin user hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        TaiKhoan user = taiKhoanRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng hiện tại"));

        return ResponseEntity.ok(thongBaoRepository.findAllByNguoiNhanOrderByThoiGianDesc(user));
    }

    @PutMapping("/{id}/da-xem")
    public ResponseEntity<Void> danhDauDaXem(@PathVariable String id) {
        ThongBao tb = thongBaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));

        // (Optional) Kiểm tra xem thông báo này có đúng là của user đang đăng nhập không để bảo mật hơn
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!tb.getNguoiNhan().getMaTaiKhoan().equals(userDetails.getId())) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        tb.setDaXem(true);
        thongBaoRepository.save(tb);

        return ResponseEntity.ok().build();
    }
}