package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.dto.StatisticsDTO;

import java.util.Map;

public interface StatisticsService {
    // Thống kê tổng quan
    StatisticsDTO getOverviewStatistics();

    // Thống kê nhân khẩu theo giới tính
    Map<String, Long> getNhanKhauByGioiTinh();

    // Thống kê nhân khẩu theo độ tuổi (0-18, 19-35, 36-50, 51-65, >65)
    Map<String, Long> getNhanKhauByDoTuoi();

    // --- [CẬP NHẬT] Thêm tham số year và quarter ---
    // Thống kê phản ánh theo trạng thái (có lọc theo quý/năm)
    Map<String, Long> getPhanAnhByTrangThai(int year, int quarter);

    // Thống kê phản ánh theo lĩnh vực
    Map<String, Long> getPhanAnhByLinhVuc();

    // Thống kê phản ánh theo mức độ khẩn cấp
    Map<String, Long> getPhanAnhByMucDoKhanCap();

    // Thống kê phản ánh theo tháng trong năm
    Map<String, Long> getPhanAnhByMonth(int year);

    // Thống kê hộ khẩu đăng ký theo tháng
    Map<String, Long> getHoKhauByMonth(int year);
}