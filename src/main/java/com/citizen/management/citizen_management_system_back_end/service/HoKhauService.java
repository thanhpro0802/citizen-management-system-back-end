package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.dto.DoiChuHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.NhapHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.TachHoRequest;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import java.util.List;

public interface HoKhauService {
    HoKhau taoMoi(HoKhau hoKhau);

    HoKhau capNhat(String maHoKhau, HoKhau hoKhauSua); // dùng String cho id

    void xoa(String maHoKhau);

    List<HoKhau> layTatCa();

    HoKhau layTheoId(String maHoKhau);

    HoKhau tachHo(String maHoCu, TachHoRequest request);

    HoKhau nhapHo(String maHoNhapVao, NhapHoRequest request);

    HoKhau doiChuHo(String maHoKhau, DoiChuHoRequest request);

    Long getCountHoKhau(String diaChi);
}
