package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.repository.HoKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.NhanKhauRepository;
import com.citizen.management.citizen_management_system_back_end.service.impl.HoKhauServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HoKhauServiceTest {

    @Mock
    private HoKhauRepository hoKhauRepository;

    @Mock
    private NhanKhauRepository nhanKhauRepository;

    @InjectMocks
    private HoKhauServiceImpl hoKhauService;

    private TaiKhoan taiKhoan;
    private NhanKhau nhanKhau;
    private HoKhau hoKhau;

    @BeforeEach
    void setUp() {
        // Setup test data
        nhanKhau = new NhanKhau();
        nhanKhau.setMaNhanKhau("NK001");
        nhanKhau.setHoTen("Nguyen Van A");

        hoKhau = new HoKhau();
        hoKhau.setMaHoKhau("HK001");
        hoKhau.setDiaChi("123 Nguyen Trai, Ha Noi");
        hoKhau.setChuHo(nhanKhau);

        nhanKhau.setHoKhau(hoKhau);

        taiKhoan = new TaiKhoan();
        taiKhoan.setMaTaiKhoan("TK001");
        taiKhoan.setNhanKhau(nhanKhau);
    }

    @Test
    void testLayHoKhauCuaToi_Success() {
        // Arrange
        when(nhanKhauRepository.findById("NK001")).thenReturn(Optional.of(nhanKhau));

        // Act
        HoKhau result = hoKhauService.layHoKhauCuaToi(taiKhoan);

        // Assert
        assertNotNull(result);
        assertEquals("HK001", result.getMaHoKhau());
        assertEquals("123 Nguyen Trai, Ha Noi", result.getDiaChi());
        verify(nhanKhauRepository, times(1)).findById("NK001");
    }

    @Test
    void testLayHoKhauCuaToi_TaiKhoanNull() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            hoKhauService.layHoKhauCuaToi(null);
        });
        assertEquals("Tài khoản không hợp lệ hoặc chưa liên kết với nhân khẩu", exception.getMessage());
    }

    @Test
    void testLayHoKhauCuaToi_NhanKhauNull() {
        // Arrange
        taiKhoan.setNhanKhau(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            hoKhauService.layHoKhauCuaToi(taiKhoan);
        });
        assertEquals("Tài khoản không hợp lệ hoặc chưa liên kết với nhân khẩu", exception.getMessage());
    }

    @Test
    void testLayHoKhauCuaToi_NhanKhauNotFound() {
        // Arrange
        when(nhanKhauRepository.findById("NK001")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            hoKhauService.layHoKhauCuaToi(taiKhoan);
        });
        assertEquals("Không tìm thấy nhân khẩu với mã: NK001", exception.getMessage());
    }

    @Test
    void testLayHoKhauCuaToi_NoHoKhau() {
        // Arrange
        nhanKhau.setHoKhau(null);
        when(nhanKhauRepository.findById("NK001")).thenReturn(Optional.of(nhanKhau));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            hoKhauService.layHoKhauCuaToi(taiKhoan);
        });
        assertEquals("Nhân khẩu chưa thuộc hộ khẩu nào", exception.getMessage());
    }

    @Test
    void testTimKiemTheoDiaChi_Success() {
        // Arrange
        List<HoKhau> expectedList = new ArrayList<>();
        expectedList.add(hoKhau);
        when(hoKhauRepository.findByDiaChiContainingIgnoreCase("Nguyen Trai")).thenReturn(expectedList);

        // Act
        List<HoKhau> result = hoKhauService.timKiemTheoDiaChi("Nguyen Trai");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("HK001", result.get(0).getMaHoKhau());
        verify(hoKhauRepository, times(1)).findByDiaChiContainingIgnoreCase("Nguyen Trai");
    }

    @Test
    void testTimKiemTheoDiaChi_EmptyKeyword() {
        // Act
        List<HoKhau> result = hoKhauService.timKiemTheoDiaChi("");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(hoKhauRepository, never()).findByDiaChiContainingIgnoreCase(anyString());
    }

    @Test
    void testTimKiemTheoDiaChi_NullKeyword() {
        // Act
        List<HoKhau> result = hoKhauService.timKiemTheoDiaChi(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(hoKhauRepository, never()).findByDiaChiContainingIgnoreCase(anyString());
    }

    @Test
    void testTimKiemTheoDiaChi_WhitespaceKeyword() {
        // Act
        List<HoKhau> result = hoKhauService.timKiemTheoDiaChi("   ");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(hoKhauRepository, never()).findByDiaChiContainingIgnoreCase(anyString());
    }
}
