package com.citizen.management.citizen_management_system_back_end.service;

import java.util.Map;

public interface ThongKeService {

    Map<String, Object> thongKeHoKhau(String phuong);
    Map<String, Object> thongKeNhanKhau(String gioiTinh);

    Map<String, Long> thongKeTuoi();
    Map<String, Long> thongKeGioiTinh();
    Map<String, Long> thongKeQueQuan();
    Map<String, Long> thongKeDanToc();
}
