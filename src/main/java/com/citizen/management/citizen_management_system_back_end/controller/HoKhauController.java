package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.model.HoKhau;
import com.citizen.management.citizen_management_system_back_end.service.HoKhauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ho-khau")
public class HoKhauController {
    @Autowired
    private HoKhauService hoKhauService;

    @PostMapping
    public HoKhau themMoi(@RequestBody HoKhau hoKhau) {
        return hoKhauService.taoMoi(hoKhau);
    }

    @PutMapping("/{id}")
    public HoKhau sua(@PathVariable Long id, @RequestBody HoKhau hoKhau) {
        return hoKhauService.capNhat(id, hoKhau);
    }

    @DeleteMapping("/{id}")
    public void xoa(@PathVariable Long id) {
        hoKhauService.xoa(id);
    }

    @GetMapping
    public List<HoKhau> xemDanhSach() {
        return hoKhauService.layTatCa();
    }

    @GetMapping("/{id}")
    public HoKhau xemChiTiet(@PathVariable Long id) {
        return hoKhauService.layTheoId(id);
    }
}