package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tai-khoan") // Đường dẫn này phải khớp với file userService.js bên Frontend
@CrossOrigin(origins = "*") // Quan trọng: Cho phép ReactJS gọi vào mà không bị chặn
public class TaiKhoanController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    // API: GET http://localhost:8080/api/v1/tai-khoan
    // Hoặc: GET http://localhost:8080/api/v1/tai-khoan?vaiTro=CAN_BO
    @GetMapping
    public ResponseEntity<?> getDanhSachTaiKhoan(@RequestParam(required = false) String vaiTro) {
        if (vaiTro != null && vaiTro.equals("CAN_BO")) {
            // Nếu Frontend gửi yêu cầu lọc cán bộ
            return ResponseEntity.ok(taiKhoanService.getDanhSachCanBo());
        }

        // Mặc định trả về hết (hoặc tùy logic bạn muốn)
        return ResponseEntity.ok(taiKhoanService.getAll());
    }
}
