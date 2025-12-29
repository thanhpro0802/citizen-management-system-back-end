package com.citizen.management.citizen_management_system_back_end.service.impl;

import lombok.AllArgsConstructor;

import com.citizen.management.citizen_management_system_back_end.dto.projection.KeyValueProjection;
import com.citizen.management.citizen_management_system_back_end.dto.projection.PhanAnhTrangThaiProjection;
import com.citizen.management.citizen_management_system_back_end.dto.projection.PhanAnhTrangThaiTheoThangProjection;
import com.citizen.management.citizen_management_system_back_end.dto.projection.PhanAnhTrangThaiTheoQuyProjection;
import com.citizen.management.citizen_management_system_back_end.dto.projection.TTTVTheoNamProjection;
import com.citizen.management.citizen_management_system_back_end.dto.projection.TamTruTamVangProjection;
import com.citizen.management.citizen_management_system_back_end.entity.NhanKhau;
import com.citizen.management.citizen_management_system_back_end.enums.EnumThongKe;
import com.citizen.management.citizen_management_system_back_end.repository.HoKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.NhanKhauRepository;
import com.citizen.management.citizen_management_system_back_end.repository.ThongKeRepository;
import com.citizen.management.citizen_management_system_back_end.service.ThongKeService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor

public class ThongKeServiceImpl implements ThongKeService {
    private final NhanKhauRepository nhanKhauRepository;
    private final HoKhauRepository hoKhauRepository;
    private final ThongKeRepository thongKeRepository;

    // THỐNG KÊ NHÂN KHẨU
    @Override
    public Map<String, Object> thongKeNhanKhau(List<EnumThongKe> types) {
        Map<String, Object> result = new LinkedHashMap<>();

        for (EnumThongKe type : types) {
            switch (type) {
                case DO_TUOI ->
                    result.put("doTuoi", thongKeTuoi());

                case GIOI_TINH ->
                    result.put("gioiTinh", thongKeGioiTinh());

                case QUE_QUAN ->
                    result.put("queQuan", thongKeQueQuan());

                case DAN_TOC ->
                    result.put("danToc", thongKeDanToc());

                case TONG_NK ->
                    result.put("tong", tongNhanKhau());
            }
        }
        return result;
    }

    private Long tongNhanKhau() {
        return thongKeRepository.count();
    }

    private Map<String, Object> thongKeTuoi() {
        Map<String, Long> raw = new LinkedHashMap<>();
        raw.put("0-17", 0L);
        raw.put("18-35", 0L);
        raw.put("36-60", 0L);
        raw.put("60+", 0L);

        LocalDate today = LocalDate.now();

        List<NhanKhau> list = nhanKhauRepository.findAll();
        for (NhanKhau nk : list) {
            LocalDate birthDate = nk.getNgaySinh().toLocalDate();
            int age = Period.between(birthDate, today).getYears();

            if (age <= 17)
                raw.put("0-17", raw.get("0-17") + 1);
            else if (age <= 35)
                raw.put("18-35", raw.get("18-35") + 1);
            else if (age <= 60)
                raw.put("36-60", raw.get("36-60") + 1);
            else
                raw.put("60+", raw.get("60+") + 1);
        }

        Map<String, Object> datasets = new HashMap<>();
        datasets.put("label", "Số nhân khẩu");
        datasets.put("data", new ArrayList<>(raw.values()));

        Map<String, Object> result = new HashMap<>();
        result.put("labels", new ArrayList<>(raw.keySet()));
        result.put("datasets", datasets);

        return result;
    }

    private Map<String, Object> thongKeGioiTinh() {
        Map<String, Long> raw = new HashMap<>();
        var list = thongKeRepository.theoGioiTinh();

        for (KeyValueProjection item : list) {
            raw.put(item.getKey(), item.getValue());
        }

        Map<String, Object> datasets = new HashMap<>();
        datasets.put("label", "Số nhân khẩu");
        datasets.put("data", new ArrayList<>(raw.values()));

        Map<String, Object> result = new HashMap<>();
        result.put("labels", new ArrayList<>(raw.keySet()));
        result.put("datasets", datasets);

        return result;
    }

    private Map<String, Object> thongKeQueQuan() {
        Map<String, Long> raw = new HashMap<>();
        var list = thongKeRepository.theoQueQuan();

        for (KeyValueProjection item : list) {
            raw.put(item.getKey(), item.getValue());
        }

        Map<String, Object> datasets = new HashMap<>();
        datasets.put("label", "Số nhân khẩu");
        datasets.put("data", new ArrayList<>(raw.values()));

        Map<String, Object> result = new HashMap<>();
        result.put("labels", new ArrayList<>(raw.keySet()));
        result.put("datasets", datasets);

        return result;
    }

    private Map<String, Object> thongKeDanToc() {
        Map<String, Long> raw = new HashMap<>();
        var list = thongKeRepository.theoDanToc();

        for (KeyValueProjection item : list) {
            raw.put(item.getKey(), item.getValue());
        }

        Map<String, Object> datasets = new HashMap<>();
        datasets.put("label", "Số nhân khẩu");
        datasets.put("data", new ArrayList<>(raw.values()));

        Map<String, Object> result = new HashMap<>();
        result.put("labels", new ArrayList<>(raw.keySet()));
        result.put("datasets", datasets);

        return result;
    }

    // THỐNG KÊ HỘ KHẨU
    @Override
    public Map<String, Object> thongKeHoKhau(List<EnumThongKe> types) {
        Map<String, Object> result = new LinkedHashMap<>();

        for (EnumThongKe type : types) {
            switch (type) {
                case SO_THANH_VIEN ->
                    result.put("soThanhVien", thongKeSoThanhVien());

                case TONG_HK ->
                    result.put("tong", tongHoKhau());
            }
        }
        return result;
    }

    private Long tongHoKhau() {
        return hoKhauRepository.count();
    }

    private Map<String, Object> thongKeSoThanhVien() {
        Map<String, Long> raw = new LinkedHashMap<>();
        raw.put("1-2", 0L);
        raw.put("3-5", 0L);
        raw.put(">5", 0L);

        var list = thongKeRepository.theoSoThanhVien();

        for (KeyValueProjection item : list) {
            Long num = item.getValue();

            if (num <= 2)
                raw.put("1-2", raw.get("1-2") + 1);
            else if (num <= 5)
                raw.put("3-5", raw.get("3-5") + 1);
            else
                raw.put(">5", raw.get(">5") + 1);
        }

        Map<String, Object> datasets = new HashMap<>();
        datasets.put("label", "Số hộ khẩu");
        datasets.put("data", new ArrayList<>(raw.values()));

        Map<String, Object> result = new HashMap<>();
        result.put("labels", new ArrayList<>(raw.keySet()));
        result.put("datasets", datasets);

        return result;
    }

    // THỐNG KÊ PHẢN ÁNH
    @Override
    public Map<String, Object> thongKePhanAnh(List<EnumThongKe> types, LocalDate startDate) {
        Map<String, Object> result = new LinkedHashMap<>();

        for (EnumThongKe type : types) {
            switch (type) {
                case TUAN ->
                    result.put("tuan", thongKePhanAnhTheoTuan(startDate));
            }
        }
        return result;
    }

    private Map<String, Object> thongKePhanAnhTheoTuan(LocalDate startDate) {
        LocalDate monday = startDate.with(DayOfWeek.MONDAY);

        LocalDateTime start = monday.atStartOfDay();
        LocalDateTime end = monday.plusDays(7).atStartOfDay();

        Map<LocalDate, PhanAnhTrangThaiProjection> raw = new HashMap<>();

        thongKeRepository.thongKeTheoNgay(start, end)
                .forEach(item -> raw.put(item.getNgay(), item));

        List<String> labels = new ArrayList<>();
        List<Long> choXuLyData = new ArrayList<>();
        List<Long> dangXuLyData = new ArrayList<>();
        List<Long> daXuLyData = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = monday.plusDays(i);
            labels.add(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));

            PhanAnhTrangThaiProjection p = raw.get(date);

            choXuLyData.add(p != null ? p.getChoXuLy() : 0);
            dangXuLyData.add(p != null ? p.getDangXuLy() : 0);
            daXuLyData.add(p != null ? p.getDaXuLy() : 0);
        }

        List<Map<String, Object>> datasets = new ArrayList<>();

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Chờ xử lý");
                put("data", choXuLyData);
            }
        });

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Đang xử lý");
                put("data", dangXuLyData);
            }
        });

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Đã xử lý");
                put("data", daXuLyData);
            }
        });

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("labels", labels);
        result.put("datasets", datasets);

        return result;
    }

    @Override
    public Map<String, Object> thongKePhanAnhTheoThang(int year) {

        Map<Integer, PhanAnhTrangThaiTheoThangProjection> raw = new HashMap<>();

        thongKeRepository.thongKeTheoThang(year)
                .forEach(item -> raw.put(item.getThang().intValue(), item));

        List<String> labels = new ArrayList<>();
        List<Long> choXuLyData = new ArrayList<>();
        List<Long> dangXuLyData = new ArrayList<>();
        List<Long> daXuLyData = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            labels.add(Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH));

            PhanAnhTrangThaiTheoThangProjection p = raw.get(month);

            choXuLyData.add(p != null ? p.getChoXuLy() : 0);
            dangXuLyData.add(p != null ? p.getDangXuLy() : 0);
            daXuLyData.add(p != null ? p.getDaXuLy() : 0);
        }

        List<Map<String, Object>> datasets = new ArrayList<>();

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Chờ xử lý");
                put("data", choXuLyData);
            }
        });

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Đang xử lý");
                put("data", dangXuLyData);
            }
        });

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Đã xử lý");
                put("data", daXuLyData);
            }
        });

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("labels", labels);
        result.put("datasets", datasets);

        return result;
    }

    @Override
    public Map<String, Object> thongKePhanAnhTheoQuy(int year) {

        Map<Integer, PhanAnhTrangThaiTheoQuyProjection> raw = new HashMap<>();

        thongKeRepository.thongKeTheoQuy(year)
                .forEach(item -> raw.put(item.getQuy(), item));

        List<String> labels = new ArrayList<>();
        List<Long> choXuLyData = new ArrayList<>();
        List<Long> dangXuLyData = new ArrayList<>();
        List<Long> daXuLyData = new ArrayList<>();

        for (int quy = 1; quy <= 4; quy++) {
            labels.add("Q" + quy);

            PhanAnhTrangThaiTheoQuyProjection p = raw.get(quy);

            choXuLyData.add(p != null ? p.getChoXuLy() : 0);
            dangXuLyData.add(p != null ? p.getDangXuLy() : 0);
            daXuLyData.add(p != null ? p.getDaXuLy() : 0);
        }

        List<Map<String, Object>> datasets = new ArrayList<>();

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Chờ xử lý");
                put("data", choXuLyData);
            }
        });

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Đang xử lý");
                put("data", dangXuLyData);
            }
        });

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Đã xử lý");
                put("data", daXuLyData);
            }
        });

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("labels", labels);
        result.put("datasets", datasets);

        return result;
    }

    @Override
    public Map<String, Object> thongKeTamTruTamVang(List<EnumThongKe> types, LocalDate startDate) {
        Map<String, Object> result = new LinkedHashMap<>();

        for (EnumThongKe type : types) {
            switch (type) {
                case TAM_TRU ->
                    result.put("tamTru", thongKeTamTru(startDate));

                case TAM_VANG ->
                    result.put("tamVang", thongKeTamVang(startDate));
            }
        }
        return result;
    }

    private Long thongKeTamTru(LocalDate startDate) {
        return thongKeRepository.countTamTru(startDate.atStartOfDay());
    }

    private Long thongKeTamVang(LocalDate startDate) {
        return thongKeRepository.countTamVang(startDate.atStartOfDay());
    }

    @Override
    public Map<String, Object> thongKeTamTruTamVangTheoTuan(List<EnumThongKe> types, LocalDate startDate) {
        Map<String, Object> result = new LinkedHashMap<>();

        for (EnumThongKe type : types) {
            switch (type) {
                case TAM_TRU ->
                    result.put("tamTru", thongKeTamTruTheoTuan(startDate));

                case TAM_VANG ->
                    result.put("tamVang", thongKeTamVangTheoTuan(startDate));
            }
        }
        return result;
    }

    private Map<String, Object> thongKeTamTruTheoTuan(LocalDate startDate) {
        LocalDate monday = startDate.with(DayOfWeek.MONDAY);
        LocalDateTime start = monday.atStartOfDay();
        LocalDateTime end = start.plusDays(7);

        Map<LocalDate, TamTruTamVangProjection> raw = new HashMap<>();

        thongKeRepository.thongKeTamTruTheoNgay(start, end)
                .forEach(item -> raw.put(item.getNgay(), item));

        List<String> labels = new ArrayList<>();
        List<Long> batDauData = new ArrayList<>();
        List<Long> ketThucData = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = monday.plusDays(i);
            labels.add(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));

            TamTruTamVangProjection p = raw.get(date);

            batDauData.add(p != null ? p.getBatDau() : 0);
            ketThucData.add(p != null ? p.getKetThuc() : 0);
        }

        List<Map<String, Object>> datasets = new ArrayList<>();

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Bắt đầu");
                put("data", batDauData);
            }
        });

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Kết thúc");
                put("data", ketThucData);
            }
        });

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("labels", labels);
        result.put("datasets", datasets);

        return result;
    }

    private Map<String, Object> thongKeTamVangTheoTuan(LocalDate startDate) {
        LocalDate monday = startDate.with(DayOfWeek.MONDAY);
        LocalDateTime start = monday.atStartOfDay();
        LocalDateTime end = start.plusDays(7);

        Map<LocalDate, TamTruTamVangProjection> raw = new HashMap<>();

        thongKeRepository.thongKeTamVangTheoNgay(start, end)
                .forEach(item -> raw.put(item.getNgay(), item));

        List<String> labels = new ArrayList<>();
        List<Long> batDauData = new ArrayList<>();
        List<Long> ketThucData = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = monday.plusDays(i);
            labels.add(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));

            TamTruTamVangProjection p = raw.get(date);

            batDauData.add(p != null ? p.getBatDau() : 0);
            ketThucData.add(p != null ? p.getKetThuc() : 0);
        }

        List<Map<String, Object>> datasets = new ArrayList<>();

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Bắt đầu");
                put("data", batDauData);
            }
        });

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Kết thúc");
                put("data", ketThucData);
            }
        });

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("labels", labels);
        result.put("datasets", datasets);

        return result;
    }

    @Override
    public Map<String, Object> thongKeTamTruTheoNam(int year) {

        LocalDateTime start = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(year, 12, 31).plusDays(1).atStartOfDay();

        Map<Integer, TTTVTheoNamProjection> raw = new HashMap<>();

        thongKeRepository.thongKeTamTruTheoThang(start, end)
                .forEach(item -> raw.put(item.getThang(), item));

        List<String> labels = new ArrayList<>();
        List<Long> batDauData = new ArrayList<>();
        List<Long> ketThucData = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {

            Month m = Month.of(month);
            labels.add(m.getDisplayName(TextStyle.SHORT, Locale.ENGLISH));

            TTTVTheoNamProjection p = raw.get(month);

            batDauData.add(p != null ? p.getBatDau() : 0);
            ketThucData.add(p != null ? p.getKetThuc() : 0);
        }

        List<Map<String, Object>> datasets = new ArrayList<>();

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Bắt đầu");
                put("data", batDauData);
            }
        });

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Kết thúc");
                put("data", ketThucData);
            }
        });

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("labels", labels);
        result.put("datasets", datasets);

        return result;
    }

    @Override
    public Map<String, Object> thongKeTamVangTheoNam(int year) {

        LocalDateTime start = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(year, 12, 31).plusDays(1).atStartOfDay();

        Map<Integer, TTTVTheoNamProjection> raw = new HashMap<>();

        thongKeRepository.thongKeTamVangTheoThang(start, end)
                .forEach(item -> raw.put(item.getThang(), item));

        List<String> labels = new ArrayList<>();
        List<Long> batDauData = new ArrayList<>();
        List<Long> ketThucData = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {

            Month m = Month.of(month);
            labels.add(m.getDisplayName(TextStyle.SHORT, Locale.ENGLISH));

            TTTVTheoNamProjection p = raw.get(month);

            batDauData.add(p != null ? p.getBatDau() : 0);
            ketThucData.add(p != null ? p.getKetThuc() : 0);
        }

        List<Map<String, Object>> datasets = new ArrayList<>();

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Bắt đầu");
                put("data", batDauData);
            }
        });

        datasets.add(new LinkedHashMap<>() {
            {
                put("label", "Kết thúc");
                put("data", ketThucData);
            }
        });

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("labels", labels);
        result.put("datasets", datasets);

        return result;
    }
}