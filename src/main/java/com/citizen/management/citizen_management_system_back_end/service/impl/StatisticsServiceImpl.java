package com.citizen.management.citizen_management_system_back_end.service.impl;

import com.citizen.management.citizen_management_system_back_end.dto.StatisticsDTO;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.entity.PhanAnh;
import com.citizen.management.citizen_management_system_back_end.enums.EnumMucDoKhanCap;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThai;
import com.citizen.management.citizen_management_system_back_end.repository.HoKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.NhanKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.PhanAnhRepository;
import com.citizen.management.citizen_management_system_back_end.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        // Tổng số hộ khẩu
        long tongHoKhau = hoKhauRepository.count();
        
        // Tổng số nhân khẩu
        long tongNhanKhau = nhanKhauRepository.count();
        
        // Tổng số phản ánh
        long tongPhanAnh = phanAnhRepository.count();
        
        // Phản ánh đang xử lý
        long phanAnhDangXuLy = phanAnhRepository.countByTrangThaiHienTai(EnumTrangThai.DANG_XU_LY);
        
        // Phản ánh đã hoàn thành
        long phanAnhHoanThanh = phanAnhRepository.countByTrangThaiHienTai(EnumTrangThai.DA_XU_LY);
        
        // Phản ánh quá hạn (trạng thái != DA_XU_LY và thời hạn xử lý < ngày hiện tại)
        long phanAnhQuaHan = phanAnhRepository.countByTrangThaiHienTaiNotAndThoiHanXuLyBefore(
            EnumTrangThai.DA_XU_LY, 
            new Date()
        );
        
        return new StatisticsDTO(
            tongHoKhau,
            tongNhanKhau,
            tongPhanAnh,
            phanAnhDangXuLy,
            phanAnhHoanThanh,
            phanAnhQuaHan
        );
    }
    
    @Override
    public Map<String, Long> getNhanKhauByGioiTinh() {
        Map<String, Long> result = new LinkedHashMap<>();
        
        // Đếm số lượng theo từng giới tính
        long nam = nhanKhauRepository.countByGioiTinh("Nam");
        long nu = nhanKhauRepository.countByGioiTinh("Nữ");
        long khac = nhanKhauRepository.countByGioiTinh("Khác");
        
        result.put("Nam", nam);
        result.put("Nữ", nu);
        result.put("Khác", khac);
        
        return result;
    }
    
    @Override
    public Map<String, Long> getNhanKhauByDoTuoi() {
        Map<String, Long> result = new LinkedHashMap<>();
        
        // Khởi tạo các nhóm tuổi
        result.put("0-18", 0L);
        result.put("19-35", 0L);
        result.put("36-50", 0L);
        result.put("51-65", 0L);
        result.put(">65", 0L);
        
        // Lấy tất cả nhân khẩu để tính độ tuổi
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
    
    @Override
    public Map<String, Long> getPhanAnhByTrangThai() {
        Map<String, Long> result = new LinkedHashMap<>();
        
        // Đếm theo từng trạng thái
        long cho = phanAnhRepository.countByTrangThaiHienTai(EnumTrangThai.CHO);
        long dangXuLy = phanAnhRepository.countByTrangThaiHienTai(EnumTrangThai.DANG_XU_LY);
        long daXuLy = phanAnhRepository.countByTrangThaiHienTai(EnumTrangThai.DA_XU_LY);
        
        result.put("CHO", cho);
        result.put("DANG_XU_LY", dangXuLy);
        result.put("DA_XU_LY", daXuLy);
        
        return result;
    }
    
    @Override
    public Map<String, Long> getPhanAnhByLinhVuc() {
        Map<String, Long> result = new LinkedHashMap<>();
        
        // Lấy tất cả phản ánh để nhóm theo lĩnh vực
        List<PhanAnh> allPhanAnh = phanAnhRepository.findAll();
        
        Map<String, Long> grouped = allPhanAnh.stream()
            .filter(pa -> pa.getLinhVuc() != null && !pa.getLinhVuc().trim().isEmpty())
            .collect(Collectors.groupingBy(
                PhanAnh::getLinhVuc,
                Collectors.counting()
            ));
        
        // Sắp xếp theo số lượng giảm dần
        grouped.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        
        return result;
    }
    
    @Override
    public Map<String, Long> getPhanAnhByMucDoKhanCap() {
        Map<String, Long> result = new LinkedHashMap<>();
        
        // Đếm theo từng mức độ khẩn cấp
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
        
        // Khởi tạo tất cả 12 tháng với giá trị 0
        for (int i = 1; i <= 12; i++) {
            result.put(String.valueOf(i), 0L);
        }
        
        // Lấy dữ liệu từ database
        List<Object[]> data = phanAnhRepository.countByMonth(year);
        
        // Điền dữ liệu vào map
        for (Object[] row : data) {
            Integer month = (Integer) row[0];
            Long count = (Long) row[1];
            if (month != null && count != null) {
                result.put(String.valueOf(month), count);
            }
        }
        
        return result;
    }
    
    @Override
    public Map<String, Long> getHoKhauByMonth(int year) {
        Map<String, Long> result = new LinkedHashMap<>();
        
        // Khởi tạo tất cả 12 tháng với giá trị 0
        for (int i = 1; i <= 12; i++) {
            result.put(String.valueOf(i), 0L);
        }
        
        // Lấy dữ liệu từ database
        List<Object[]> data = hoKhauRepository.countByMonth(year);
        
        // Điền dữ liệu vào map
        for (Object[] row : data) {
            Integer month = (Integer) row[0];
            Long count = (Long) row[1];
            if (month != null && count != null) {
                result.put(String.valueOf(month), count);
            }
        }
        
        return result;
    }
}
