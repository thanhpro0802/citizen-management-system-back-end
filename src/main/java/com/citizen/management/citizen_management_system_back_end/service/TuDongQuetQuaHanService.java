package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.entity.PhanAnh;
import com.citizen.management.citizen_management_system_back_end.entity.ThongBao;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThai;
import com.citizen.management.citizen_management_system_back_end.repository.PhanAnhRepository;
import com.citizen.management.citizen_management_system_back_end.repository.ThongBaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TuDongQuetQuaHanService {

    private final PhanAnhRepository phanAnhRepository;
    private final ThongBaoRepository thongBaoRepository;

    // Cron expression: Giây - Phút - Giờ - Ngày - Tháng - Thứ
    // "0 0 7 * * ?" nghĩa là chạy vào 7:00:00 sáng mỗi ngày
    @Scheduled(cron = "0 0 7 * * ?")
    //@Scheduled(fixedRate = 10000)
    @Transactional
    public void quetHoSoQuaHan() {
        System.out.println("--- BẮT ĐẦU QUÉT HỒ SƠ QUÁ HẠN ---");

        Date homNay = new Date();

        // 1. Tìm các hồ sơ chưa xong và đã quá hạn
        List<PhanAnh> listQuaHan = phanAnhRepository.findByTrangThaiHienTaiNotAndThoiHanXuLyBefore(
                EnumTrangThai.DA_XU_LY,
                homNay
        );

        for (PhanAnh pa : listQuaHan) {
            // Chỉ gửi nếu hồ sơ đã có người phụ trách
            if (pa.getCanBoPhuTrach() != null) {

                // (Tùy chọn) Kiểm tra xem hôm nay đã gửi thông báo cho hồ sơ này chưa để tránh spam
                // Nhưng đơn giản nhất là cứ gửi để nhắc nhở quyết liệt

                ThongBao tb = new ThongBao();
                tb.setNguoiNhan(pa.getCanBoPhuTrach());
                tb.setNoiDung("⚠️ CẢNH BÁO QUÁ HẠN: Hồ sơ '" + pa.getTieuDe() + "' đã vượt quá thời hạn xử lý!");
                tb.setThoiGian(new Date());
                tb.setDaXem(false);
                tb.setMaPhanAnhLienQuan(pa.getMaPhanAnh());

                thongBaoRepository.save(tb);

                System.out.println("Đã gửi cảnh báo cho cán bộ: " + pa.getCanBoPhuTrach().getCccd());
            }
        }

        System.out.println("--- KẾT THÚC QUÉT: Đã xử lý " + listQuaHan.size() + " hồ sơ ---");
    }
}
