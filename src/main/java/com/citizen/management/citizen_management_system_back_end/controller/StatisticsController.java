package com.citizen.management.citizen_management_system_back_end.controller;

import com.citizen.management.citizen_management_system_back_end.dto.StatisticsDTO;
import com.citizen.management.citizen_management_system_back_end.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/overview")
    public ResponseEntity<StatisticsDTO> getOverview() {
        StatisticsDTO statistics = statisticsService.getOverviewStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/nhan-khau/gioi-tinh")
    public ResponseEntity<Map<String, Long>> getNhanKhauByGioiTinh() {
        Map<String, Long> statistics = statisticsService.getNhanKhauByGioiTinh();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/nhan-khau/do-tuoi")
    public ResponseEntity<Map<String, Long>> getNhanKhauByDoTuoi() {
        Map<String, Long> statistics = statisticsService.getNhanKhauByDoTuoi();
        return ResponseEntity.ok(statistics);
    }

    // --- [CẬP NHẬT] Thêm tham số year và quarter ---
    @GetMapping("/phan-anh/trang-thai")
    public ResponseEntity<Map<String, Long>> getPhanAnhByTrangThai(
            @RequestParam(defaultValue = "2025") int year,
            @RequestParam(defaultValue = "1") int quarter
    ) {
        // Truyền tham số xuống Service
        Map<String, Long> statistics = statisticsService.getPhanAnhByTrangThai(year, quarter);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/phan-anh/linh-vuc")
    public ResponseEntity<Map<String, Long>> getPhanAnhByLinhVuc() {
        Map<String, Long> statistics = statisticsService.getPhanAnhByLinhVuc();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/phan-anh/muc-do")
    public ResponseEntity<Map<String, Long>> getPhanAnhByMucDo() {
        Map<String, Long> statistics = statisticsService.getPhanAnhByMucDoKhanCap();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/phan-anh/theo-thang")
    public ResponseEntity<Map<String, Long>> getPhanAnhByMonth(@RequestParam int year) {
        Map<String, Long> statistics = statisticsService.getPhanAnhByMonth(year);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/ho-khau/theo-thang")
    public ResponseEntity<Map<String, Long>> getHoKhauByMonth(@RequestParam int year) {
        Map<String, Long> statistics = statisticsService.getHoKhauByMonth(year);
        return ResponseEntity.ok(statistics);
    }
}