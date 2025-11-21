package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.dto.*;
import com.citizen.management.citizen_management_system_back_end.entity.PhanAnh;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.repository.TaiKhoanRepository;
import com.citizen.management.citizen_management_system_back_end.service.IPhanAnhService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/phan-anh")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PhanAnhController {
    private final IPhanAnhService phanAnhService;
    private final TaiKhoanRepository taiKhoanRepository;

    @PostMapping
    public ResponseEntity<PhanAnh> guiPhanAnhMoi(@RequestBody GuiPhanAnhRequest request) {
        //Temp
        TaiKhoan nguoiGui = taiKhoanRepository.findById("user123").orElseThrow(() -> new RuntimeException("Khong tim thay tai khoan test 'user123'. Them vao CSDL di."));
        PhanAnh paMoi = phanAnhService.guiPhanAnh(request, nguoiGui);

        return new ResponseEntity<>(paMoi, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/phan-cong")
    public ResponseEntity<PhanAnh> phanCongXuLy(@PathVariable String id, @RequestBody PhanCongRequest request) {
        //Temp
        TaiKhoan nguoiPhanCong = taiKhoanRepository.findById("canbo123").orElseThrow(() -> new EntityNotFoundException("Khong tim thay tai khoan test 'canbo123'. Them vao CSDL di."));
        PhanAnh paCapNhat = phanAnhService.phanCongXuLy(id, request, nguoiPhanCong);

        return ResponseEntity.ok(paCapNhat);
    }

    @PostMapping("/{id}/xu-ly-noi-bo")
    public ResponseEntity<Void> capNhatXuLyNoiBo(@PathVariable String id, @RequestBody XuLyNoiBoRequest request) {
        //Temp
        TaiKhoan canBoXuLy = taiKhoanRepository.findById("canbo123").orElseThrow(() -> new EntityNotFoundException("Khong tim thay tai khoan 'canbo123'. Them vao CSDL di."));
        phanAnhService.capNhatXuLyNoiBo(id, request, canBoXuLy);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/phan-hoi")
    public ResponseEntity<PhanAnh> phanHoiCongDan(@PathVariable String id, @RequestBody PhanHoiRequest request) {
        //Temp
        TaiKhoan canBoPhanHoi = taiKhoanRepository.findById("canbo123").orElseThrow(() -> new EntityNotFoundException("Khong tim thay tai khoan 'canbo123'. Them vao CSDL di."));
        PhanAnh paCapNhat = phanAnhService.phanHoiCongDan(id, request, canBoPhanHoi);

        return ResponseEntity.ok(paCapNhat);
    }

    @PutMapping("/{id}/danh-gia")
    public ResponseEntity<PhanAnh> danhGiaPhanHoi(@PathVariable String id, @RequestBody DanhGiaRequest request) {
        //Temp
        TaiKhoan nguoiDanhGia = taiKhoanRepository.findById("user123").orElseThrow(() -> new EntityNotFoundException("Khong tim thay tai khoan 'canbo123'. Them vao CSDL di."));
        PhanAnh paCapNhat = phanAnhService.danhGiaPhanHoi(id, request, nguoiDanhGia);

        return ResponseEntity.ok(paCapNhat);
    }

    @GetMapping("/cua-toi")
    public ResponseEntity<List<PhanAnh>> layDanhSachCuaToi() {
        //Temp
        TaiKhoan nguoiGui = taiKhoanRepository.findById("user123").orElseThrow(() -> new RuntimeException("Không tìm thấy user test 'user123'"));
        List<PhanAnh> danhSach = phanAnhService.layDanhSachPhanAnhCuaToi(nguoiGui);

        return ResponseEntity.ok(danhSach);
    }
}
