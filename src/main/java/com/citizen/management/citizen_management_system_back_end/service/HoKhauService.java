package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.dto.DoiChuHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.NhapHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.TachHoRequest;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import java.util.List;

public interface HoKhauService {
    HoKhau taoMoi(HoKhau hoKhau);
    HoKhau capNhat(Long id, HoKhau hoKhauSua);
    void xoa(Long id);
    List<HoKhau> layTatCa();
    HoKhau layTheoId(Long id);
    public HoKhau tachHo(Long idHoCu, TachHoRequest request);
    public HoKhau nhapHo(Long idHoNhapVao, NhapHoRequest request);
    public HoKhau doiChuHo(Long idHoKhau, DoiChuHoRequest request);
    // Nghiệp vụ nâng cao sẽ bổ sung sau: tách hộ, nhập hộ, đổi chủ hộ
}
