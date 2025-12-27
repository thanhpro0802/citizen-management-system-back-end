package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.repository.HoKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.NhanKhauRepository;
import com.citizen.management.citizen_management_system_back_end.service.impl.HoKhauServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class HoKhauServiceBugTest {

    @Mock
    private HoKhauRepository hoKhauRepository;

    @Mock
    private NhanKhauRepository nhanKhauRepository;

    @InjectMocks
    private HoKhauServiceImpl hoKhauService;

    @Test
    @DisplayName("Test tìm lỗi: Mong đợi trả về null khi chưa có hộ khẩu (để FE hiện 404), nhưng code hiện tại ném lỗi 500")
    public void testLayHoKhauCuaToi_KhiChuaCoHoKhau_PhaiTraVeNull() {
        // 1. GIẢ LẬP DỮ LIỆU (MOCK)
        String maNhanKhau = "NK001";

        // Tạo nhân khẩu nhưng KHÔNG set hộ khẩu (hoKhau = null)
        NhanKhau nhanKhauMock = new NhanKhau();
        nhanKhauMock.setMaNhanKhau(maNhanKhau);
        nhanKhauMock.setHoKhau(null); // Giả lập người này chưa có hộ khẩu

        // Tạo tài khoản liên kết với nhân khẩu đó
        TaiKhoan taiKhoanMock = new TaiKhoan();
        taiKhoanMock.setNhanKhau(nhanKhauMock);

        // Khi repository tìm nhân khẩu theo ID, trả về nhân khẩu mock ở trên
        Mockito.when(nhanKhauRepository.findById(maNhanKhau)).thenReturn(Optional.of(nhanKhauMock));

        // 2. CHẠY TEST & KIỂM TRA (ASSERTION)

        // --- KỊCH BẢN MONG ĐỢI (ĐỂ FRONTEND CHẠY ĐÚNG) ---
        // Chúng ta mong đợi service trả về null để Controller trả về 404 Not Found.
        // Tuy nhiên, bài test này SẼ FAILED (thất bại) với code hiện tại.
        try {
            HoKhau result = hoKhauService.layHoKhauCuaToi(taiKhoanMock);
            Assertions.assertNull(result, "Lỗi: Service phải trả về null khi không có hộ khẩu, không được ném Exception");
        } catch (RuntimeException e) {
            // Nếu chạy vào đây nghĩa là Bug đã được tìm thấy!
            System.out.println(">>> ĐÃ TÌM THẤY LỖI: " + e.getMessage());
            // Fail bài test để báo hiệu code đang sai
            Assertions.fail("Code hiện tại đang ném RuntimeException (Lỗi 500) thay vì trả về null (Lỗi 404): " + e.getMessage());
        }
    }
}