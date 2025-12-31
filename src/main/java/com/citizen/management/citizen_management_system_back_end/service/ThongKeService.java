package com.citizen.management.citizen_management_system_back_end.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.citizen.management.citizen_management_system_back_end.enums.EnumThongKe;

public interface ThongKeService {

    Map<String, Object> thongKeNhanKhau(List<EnumThongKe> types);

    Map<String, Object> thongKeHoKhau(List<EnumThongKe> types);

    Map<String, Object> thongKePhanAnh(List<EnumThongKe> types, LocalDate startDate);

    Map<String, Object> thongKePhanAnhTheoThang(int year);

    Map<String, Object> thongKePhanAnhTheoQuy(int year);

    Map<String, Object> thongKeTamTruTamVang(List<EnumThongKe> types, LocalDate startDate);

    Map<String, Object> thongKeTamTruTamVangTheoTuan(List<EnumThongKe> types, LocalDate startDate);

    Map<String, Object> thongKeTamTruTheoNam(int year);

    Map<String, Object> thongKeTamVangTheoNam(int year);

}