package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.model.HoKhau;
import java.util.List;

public interface HoKhauService {
    HoKhau taoMoi(HoKhau hoKhau);
    HoKhau capNhat(Long id, HoKhau hoKhauSua);
    void xoa(Long id);
    List<HoKhau> layTatCa();
    HoKhau layTheoId(Long id);

    // Nghiệp vụ nâng cao sẽ bổ sung sau: tách hộ, nhập hộ, đổi chủ hộ
}
