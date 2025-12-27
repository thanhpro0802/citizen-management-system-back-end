package com.citizen.management.citizen_management_system_back_end.repository;

import com.citizen.management.citizen_management_system_back_end.entity.PhanAnh;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThai;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles; // Thêm import này nếu dùng profile test

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// @ActiveProfiles("test") // Bỏ comment nếu bạn đang dùng file application-test.properties
public class PhanAnhRepositoryTest {

    @Autowired
    private PhanAnhRepository phanAnhRepository;

    @Test
    @DisplayName("Test đếm phản ánh quá hạn")
    @Rollback(true)
    public void testCountByTrangThaiHienTaiNotAndThoiHanXuLyBefore() {
        Date homNay = new Date();

        // BƯỚC 1: Đếm dữ liệu cũ đang có trong DB để làm mốc so sánh
        long initialCount = phanAnhRepository.countByTrangThaiHienTaiNotAndThoiHanXuLyBefore(EnumTrangThai.DA_XU_LY, homNay);
        System.out.println(">>> Số lượng ban đầu trong DB: " + initialCount);

        // --- Chuẩn bị ngày tháng ---
        Date homQua = Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date ngayMai = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Case 1: Quá hạn (Đang xử lý + Hạn hôm qua) -> CẦN ĐẾM THÊM
        PhanAnh pa1 = new PhanAnh();
        // Không set ID thủ công
        pa1.setTrangThaiHienTai(EnumTrangThai.DANG_XU_LY);
        pa1.setThoiHanXuLy(homQua);
        pa1.setTieuDe("Test quá hạn");
        phanAnhRepository.save(pa1);

        // Case 2: Đã xử lý (Dù hạn hôm qua nhưng đã xong) -> KHÔNG ĐẾM
        PhanAnh pa2 = new PhanAnh();
        pa2.setTrangThaiHienTai(EnumTrangThai.DA_XU_LY);
        pa2.setThoiHanXuLy(homQua);
        pa2.setTieuDe("Test đã xong");
        phanAnhRepository.save(pa2);

        // Case 3: Chưa quá hạn (Hạn là ngày mai) -> KHÔNG ĐẾM
        PhanAnh pa3 = new PhanAnh();
        pa3.setTrangThaiHienTai(EnumTrangThai.DANG_XU_LY);
        pa3.setThoiHanXuLy(ngayMai);
        pa3.setTieuDe("Test chưa quá hạn");
        phanAnhRepository.save(pa3);

        // BƯỚC 2: Gọi hàm cần test lần nữa
        long newCount = phanAnhRepository.countByTrangThaiHienTaiNotAndThoiHanXuLyBefore(EnumTrangThai.DA_XU_LY, homNay);
        System.out.println(">>> Số lượng sau khi insert: " + newCount);

        // BƯỚC 3: Kiểm tra
        // Logic đúng là số lượng mới phải nhiều hơn số lượng cũ đúng 1 đơn vị (là cái pa1)
        assertEquals(initialCount + 1, newCount, "Hàm đếm quá hạn hoạt động không đúng!");
    }
}