package com.citizen.management.citizen_management_system_back_end.controller;

import lombok.AllArgsConstructor;
import com.citizen.management.citizen_management_system_back_end.dto.HoKhauDto;
import com.citizen.management.citizen_management_system_back_end.service.HoKhauService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/DanhSachHoKhau")

public class HoKhauController {
    private HoKhauService hoKhauService;

    // Build Add API
    @PostMapping
    public ResponseEntity<HoKhauDto> createHoKhau(@RequestBody HoKhauDto hoKhauDto) {
        HoKhauDto savedHoKhau = hoKhauService.createHoKhau(hoKhauDto);
        return new ResponseEntity<>(savedHoKhau, HttpStatus.CREATED);
    }

    // Build Get API
    @GetMapping("{id}")
    public ResponseEntity<HoKhauDto> getHoKhau(@PathVariable("id") Long hoKhauId) {
        HoKhauDto hoKhauDto = hoKhauService.getHoKhauById(hoKhauId);
        return ResponseEntity.ok(hoKhauDto);
    }

    // Build Get All API
    @GetMapping
    public ResponseEntity<List<HoKhauDto>> getAllHoKhau() {
        List<HoKhauDto> listHoKhau = hoKhauService.getAllHoKhau();
        return ResponseEntity.ok(listHoKhau);
    }

    // Build Update API
    @PutMapping("{id}")
    public ResponseEntity<HoKhauDto> updateHoKhau(@PathVariable("id") Long hoKhauId,
                                                  @RequestBody HoKhauDto hoKhauDto) {
        HoKhauDto updatedHoKhau = hoKhauService.updateHoKhau(hoKhauId, hoKhauDto);
        return ResponseEntity.ok(updatedHoKhau);
    }

    // Build Delete API
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteHoKhau(@PathVariable("id") Long hoKhauId) {
        hoKhauService.deleteHoKhauById(hoKhauId);
        return ResponseEntity.ok("Xoa ho khau thanh cong!");
    }
}
