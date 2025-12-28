package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.repository.HoKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.NhanKhauRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

@DataJpaTest // Chỉ load JPA/Hibernate để test DB
public class HoKhauServiceIntegrationTest {

    @Autowired
    private HoKhauRepository hoKhauRepository;

    @Autowired
    private NhanKhauRepository nhanKhauRepository;

    @Test
    public void testXoaHoKhau_GayLoi_TransientObjectException() {
        // 1. CHUẨN BỊ DỮ LIỆU ĐÚNG (Persistent)
        // Tạo một nhân khẩu đã lưu (đúng luật)
        NhanKhau chuHoCu = new NhanKhau();
        chuHoCu.setHoTen("Nguyen Van A");
        nhanKhauRepository.save(chuHoCu);

        // Tạo hộ khẩu và gán chủ hộ
        HoKhau hoKhau = new HoKhau();
        hoKhau.setMaHoKhau("HK001");
        hoKhau.setChuHo(chuHoCu);
        hoKhauRepository.save(hoKhau);

        // 2. TẠO TÌNH HUỐNG GÂY LỖI (Transient)
        // Giả lập logic sai: Gán một chủ hộ MỚI nhưng QUÊN save() trước khi xóa hộ khẩu
        NhanKhau chuHoMoi_ChuaLuu = new NhanKhau();
        chuHoMoi_ChuaLuu.setHoTen("Nguoi Nao Do");
        // QUAN TRỌNG: Không gọi nhanKhauRepository.save(chuHoMoi_ChuaLuu);

        // Gán object chưa lưu vào object đã lưu
        hoKhau.setChuHo(chuHoMoi_ChuaLuu);

        // 3. THỰC HIỆN XÓA VÀ MONG ĐỢI LỖI
        // Khi gọi delete, Hibernate check state và thấy 'chuHoMoi_ChuaLuu' là transient -> Bùm!
        Exception exception = Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            hoKhauRepository.delete(hoKhau);
            hoKhauRepository.flush(); // Bắt buộc Hibernate thực thi ngay lập tức để thấy lỗi
        });

        System.out.println("Đã bắt được lỗi mong muốn: " + exception.getMessage());
    }
}