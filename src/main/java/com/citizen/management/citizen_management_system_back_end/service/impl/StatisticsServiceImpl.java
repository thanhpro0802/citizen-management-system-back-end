package com.citizen.management.citizen_management_system_back_end.service.impl;

import com.citizen.management.citizen_management_system_back_end.dto.StatisticsDTO;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.entity.PhanAnh;
import com.citizen.management.citizen_management_system_back_end.enums.EnumMucDoKhanCap;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThai;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThaiNhanKhau;
import com.citizen.management.citizen_management_system_back_end.repository.HoKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.NhanKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.PhanAnhRepository;
import com.citizen.management.citizen_management_system_back_end.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime; // Cần thêm import này
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final HoKhauRepository hoKhauRepository;
    private final NhanKhauRepository nhanKhauRepository;
    private final PhanAnhRepository phanAnhRepository;

    @Override
    public StatisticsDTO getOverviewStatistics() {
        StatisticsDTO dto = new StatisticsDTO();

        // 1. Số liệu cơ bản
        dto.setTongHoKhau(hoKhauRepository.count());
        dto.setTongNhanKhau(nhanKhauRepository.count());
        dto.setTongPhanAnh(phanAnhRepository.count());

        // 2. Số liệu Phản ánh
        dto.setPhanAnhDangXuLy(phanAnhRepository.countByTrangThaiHienTai(EnumTrangThai.DANG_XU_LY));
        dto.setPhanAnhHoanThanh(phanAnhRepository.countByTrangThaiHienTai(EnumTrangThai.DA_XU_LY));

        // Phản ánh quá hạn
        long phanAnhQuaHan = phanAnhRepository.countByTrangThaiHienTaiNotAndThoiHanXuLyBefore(
                EnumTrangThai.DA_XU_LY,
                new Date()
        );
        dto.setPhanAnhQuaHan(phanAnhQuaHan);

        // 3. Số liệu Tạm trú / Tạm vắng
        long tongTamTru = nhanKhauRepository.countByTrangThai(EnumTrangThaiNhanKhau.TAM_TRU);
        long tongTamVang = nhanKhauRepository.countByTrangThai(EnumTrangThaiNhanKhau.TAM_VANG);

        dto.setTongTamTru(tongTamTru);
        dto.setTongTamVang(tongTamVang);

        return dto;
    }

    @Override
    public Map<String, Long> getNhanKhauByGioiTinh() {
        Map<String, Long> result = new LinkedHashMap<>();

        long nam = nhanKhauRepository.countByGioiTinh("Nam");
        long nu = nhanKhauRepository.countByGioiTinh("Nữ");
        long khac = nhanKhauRepository.count() - (nam + nu);

        result.put("Nam", nam);
        result.put("Nữ", nu);
        result.put("Khác", khac > 0 ? khac : 0);

        return result;
    }

    @Override
    public Map<String, Long> getNhanKhauByDoTuoi() {
        Map<String, Long> result = new LinkedHashMap<>();

        result.put("0-18", 0L);
        result.put("19-35", 0L);
        result.put("36-50", 0L);
        result.put("51-65", 0L);
        result.put(">65", 0L);

        List<NhanKhau> allNhanKhau = nhanKhauRepository.findAll();
        LocalDate now = LocalDate.now();

        for (NhanKhau nk : allNhanKhau) {
            if (nk.getNgaySinh() != null) {
                LocalDate ngaySinh = nk.getNgaySinh().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                int age = Period.between(ngaySinh, now).getYears();

                if (age >= 0 && age <= 18) {
                    result.put("0-18", result.get("0-18") + 1);
                } else if (age >= 19 && age <= 35) {
                    result.put("19-35", result.get("19-35") + 1);
                } else if (age >= 36 && age <= 50) {
                    result.put("36-50", result.get("36-50") + 1);
                } else if (age >= 51 && age <= 65) {
                    result.put("51-65", result.get("51-65") + 1);
                } else {
                    result.put(">65", result.get(">65") + 1);
                }
            }
        }

        return result;
    }

    // --- CẬP NHẬT: Thêm tham số year và quarter ---
    @Override
    public Map<String, Long> getPhanAnhByTrangThai(int year, int quarter) {
        Map<String, Long> result = new LinkedHashMap<>();

        // 1. Tính toán ngày bắt đầu và kết thúc dựa trên Quý và Năm
        LocalDate startLocalDate;
        LocalDate endLocalDate;

        switch (quarter) {
            case 1: // Quý 1: Tháng 1-3
                startLocalDate = LocalDate.of(year, 1, 1);
                endLocalDate = LocalDate.of(year, 3, 31);
                break;
            case 2: // Quý 2: Tháng 4-6
                startLocalDate = LocalDate.of(year, 4, 1);
                endLocalDate = LocalDate.of(year, 6, 30);
                break;
            case 3: // Quý 3: Tháng 7-9
                startLocalDate = LocalDate.of(year, 7, 1);
                endLocalDate = LocalDate.of(year, 9, 30);
                break;
            case 4: // Quý 4: Tháng 10-12
                startLocalDate = LocalDate.of(year, 10, 1);
                endLocalDate = LocalDate.of(year, 12, 31);
                break;
            default:
                throw new IllegalArgumentException("Quý không hợp lệ: " + quarter);
        }

        // 2. Chuyển đổi LocalDate sang java.util.Date (Start of Day & End of Day)
        Date startDate = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endLocalDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        // 3. Gọi Repository với khoảng thời gian đã tính (Cần đảm bảo Repository đã có hàm này)
        long cho = phanAnhRepository.countByTrangThaiHienTaiAndThoiGianTaoBetween(EnumTrangThai.CHO, startDate, endDate);
        long dangXuLy = phanAnhRepository.countByTrangThaiHienTaiAndThoiGianTaoBetween(EnumTrangThai.DANG_XU_LY, startDate, endDate);
        long daXuLy = phanAnhRepository.countByTrangThaiHienTaiAndThoiGianTaoBetween(EnumTrangThai.DA_XU_LY, startDate, endDate);

        result.put("CHO", cho);
        result.put("DANG_XU_LY", dangXuLy);
        result.put("DA_XU_LY", daXuLy);

        return result;
    }

    @Override
    public Map<String, Long> getPhanAnhByLinhVuc() {
        Map<String, Long> result = new LinkedHashMap<>();

        List<PhanAnh> allPhanAnh = phanAnhRepository.findAll();

        Map<String, Long> grouped = allPhanAnh.stream()
                .filter(pa -> pa.getLinhVuc() != null && !pa.getLinhVuc().trim().isEmpty())
                .collect(Collectors.groupingBy(
                        PhanAnh::getLinhVuc,
                        Collectors.counting()
                ));

        grouped.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }

    @Override
    public Map<String, Long> getPhanAnhByMucDoKhanCap() {
        Map<String, Long> result = new LinkedHashMap<>();

        long thap = phanAnhRepository.countByMucDoKhanCap(EnumMucDoKhanCap.THAP);
        long trungBinh = phanAnhRepository.countByMucDoKhanCap(EnumMucDoKhanCap.TRUNG_BINH);
        long cao = phanAnhRepository.countByMucDoKhanCap(EnumMucDoKhanCap.CAO);

        result.put("THAP", thap);
        result.put("TRUNG_BINH", trungBinh);
        result.put("CAO", cao);

        return result;
    }

    @Override
    public Map<String, Long> getPhanAnhByMonth(int year) {
        Map<String, Long> result = new LinkedHashMap<>();

        for (int i = 1; i <= 12; i++) {
            result.put(String.valueOf(i), 0L);
        }

        List<Object[]> data = phanAnhRepository.countByMonth(year);

        for (Object[] row : data) {
            int month = ((Number) row[0]).intValue();
            long count = ((Number) row[1]).longValue();
            result.put(String.valueOf(month), count);
        }

        return result;
    }

    @Override
    public Map<String, Long> getHoKhauByMonth(int year) {
        Map<String, Long> result = new LinkedHashMap<>();

        for (int i = 1; i <= 12; i++) {
            result.put(String.valueOf(i), 0L);
        }

        List<Object[]> data = hoKhauRepository.countByMonth(year);

        for (Object[] row : data) {
            int month = ((Number) row[0]).intValue();
            long count = ((Number) row[1]).longValue();
            result.put(String.valueOf(month), count);
        }

        return result;
    }
}