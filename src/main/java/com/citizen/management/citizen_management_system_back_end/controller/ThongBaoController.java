package com.citizen.management.citizen_management_system_back_end.controller;


import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.entity.ThongBao;
import com.citizen.management.citizen_management_system_back_end.repository.TaiKhoanRepository;
import com.citizen.management.citizen_management_system_back_end.repository.ThongBaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        //Temp
        TaiKhoan user = taiKhoanRepository.findById("user123").orElseThrow();

        return ResponseEntity.ok(thongBaoRepository.findAllByNguoiNhanOrderByThoiGianDesc(user));
    }

    @PutMapping("/{id}/da-xem")
    public ResponseEntity<Void> danhDauDaXem(@PathVariable String id) {
        ThongBao tb = thongBaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay thong bao"));
        tb.setDaXem(true);
        thongBaoRepository.save(tb);

        return ResponseEntity.ok().build();
    }
}
