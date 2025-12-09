package com.citizen.management.citizen_management_system_back_end.service;

import java.util.List;
import java.util.Map;

public interface ThongKeService {

    Map<String, Object> thongKeHoKhau(String phuong);

    Map<String, Object> thongKeNhanKhau(String gioiTinh);

    Map<String, Long> thongKeDanToc();

    List<Map<String, Object>> thongKeTuoi();

    List<Map<String, Object>> thongKeGioiTinh();

    List<Map<String, Object>> thongKeQueQuan();
}
