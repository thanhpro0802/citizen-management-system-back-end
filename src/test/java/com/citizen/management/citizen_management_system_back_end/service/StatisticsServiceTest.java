package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.dto.StatisticsDTO;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThai;
import com.citizen.management.citizen_management_system_back_end.repository.HoKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.NhanKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.PhanAnhRepository;
import com.citizen.management.citizen_management_system_back_end.service.impl.StatisticsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock private HoKhauRepository hoKhauRepository;
    @Mock private NhanKhauRepository nhanKhauRepository;
    @Mock private PhanAnhRepository phanAnhRepository;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @Test
    void testGetOverviewStatistics() {
        // Mock dữ liệu cơ bản
        when(hoKhauRepository.count()).thenReturn(10L);
        when(nhanKhauRepository.count()).thenReturn(50L);
        when(phanAnhRepository.count()).thenReturn(5L);
        when(phanAnhRepository.countByTrangThaiHienTai(EnumTrangThai.DANG_XU_LY)).thenReturn(2L);
        when(phanAnhRepository.countByTrangThaiHienTai(EnumTrangThai.DA_XU_LY)).thenReturn(3L);

        // Mock hàm đếm quá hạn (Khớp với tên hàm trong Repository của bạn)
        when(phanAnhRepository.countByTrangThaiHienTaiNotAndThoiHanXuLyBefore(
                eq(EnumTrangThai.DA_XU_LY), any(Date.class))
        ).thenReturn(1L);

        // Chạy service
        StatisticsDTO result = statisticsService.getOverviewStatistics();

        // Kiểm tra
        assertEquals(10L, result.getTongHoKhau());
        assertEquals(50L, result.getTongNhanKhau());
        assertEquals(1L, result.getPhanAnhQuaHan());
    }

    @Test
    void testGetNhanKhauByGioiTinh() {
        // Mock hàm countByGioiTinh (khớp với code hiện tại của bạn)
        when(nhanKhauRepository.countByGioiTinh("Nam")).thenReturn(10L);
        when(nhanKhauRepository.countByGioiTinh("Nữ")).thenReturn(15L);
        when(nhanKhauRepository.countByGioiTinh("Khác")).thenReturn(2L);

        Map<String, Long> result = statisticsService.getNhanKhauByGioiTinh();

        assertEquals(10L, result.get("Nam"));
        assertEquals(15L, result.get("Nữ"));
        assertEquals(2L, result.get("Khác"));
    }
}