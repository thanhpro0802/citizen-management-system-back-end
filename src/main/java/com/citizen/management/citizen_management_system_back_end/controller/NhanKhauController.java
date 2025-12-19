package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.dto.*;
import com.citizen.management.citizen_management_system_back_end.service.NhanKhauService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonIgnore; // [Thêm import này]

@RequiredArgsConstructor
@CrossOrigin("*")
@RestController
@RequestMapping("/api/nhan-khau")

public class NhanKhauController {

    private final NhanKhauService nhanKhauService;

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
            @RequestParam(defaultValue = "asc") String sortDir) {
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
    public ResponseEntity<TamVangDto> registerTamVang(@PathVariable("ma") String ma,
            @Valid @RequestBody TamVangDto dto) {
        dto.setMaNhanKhau(ma);
        return ResponseEntity.ok(nhanKhauService.registerTamVang(dto));
    }

    // Khai tử
    @PostMapping("/{ma}/khai-tu")
    public ResponseEntity<NhanKhauDto> declareDeath(@PathVariable("ma") String ma) {
        return ResponseEntity.ok(nhanKhauService.declareDeath(ma));
    }
}
