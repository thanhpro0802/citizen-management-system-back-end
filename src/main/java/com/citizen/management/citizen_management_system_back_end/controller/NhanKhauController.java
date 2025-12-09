package com.citizen.management.citizen_management_system_back_end.controller;

import lombok.AllArgsConstructor;
import com.citizen.management.citizen_management_system_back_end.dto.NhanKhauDto;
import com.citizen.management.citizen_management_system_back_end.service.NhanKhauService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/nhan-khau")

public class NhanKhauController {

    private NhanKhauService nhanKhauService;

    @PostMapping
    public ResponseEntity<NhanKhauDto> createNhanKhau(@RequestBody NhanKhauDto nhanKhauDto) {
        NhanKhauDto savedNhanKhau = nhanKhauService.createNhanKhau(nhanKhauDto);
        return new ResponseEntity<>(savedNhanKhau, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<NhanKhauDto> getNhanKhauById(@PathVariable("id") Long nhanKhauId) {
        NhanKhauDto nhanKhauDto = nhanKhauService.getNhanKhauById(nhanKhauId);
        return ResponseEntity.ok(nhanKhauDto);
    }

    @GetMapping
    public ResponseEntity<List<NhanKhauDto>> getAllNhanKhau() {
        List<NhanKhauDto> listNhanKhau = nhanKhauService.getAllNhanKhau();
        return ResponseEntity.ok(listNhanKhau);
    }

    @PutMapping("{id}")
    public ResponseEntity<NhanKhauDto> updateNhanKhau(@PathVariable("id") Long nhanKhauId,
            @RequestBody NhanKhauDto nhanKhauDto) {
        NhanKhauDto updatedNhanKhau = nhanKhauService.updateNhanKhau(nhanKhauId, nhanKhauDto);
        return ResponseEntity.ok(updatedNhanKhau);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteNhanKhau(@PathVariable("id") Long nhanKhauId) {
        nhanKhauService.deleteNhanKhau(nhanKhauId);
        return ResponseEntity.ok("Xoa nhan khau thanh cong!");
    }
}
