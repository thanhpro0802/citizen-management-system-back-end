package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.dto.request.DoiChuHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.NhapHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.TachHoRequest;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
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
    
    // Phương thức mới cho phân quyền
    HoKhau layHoKhauCuaToi(TaiKhoan taiKhoan);
    List<HoKhau> timKiemTheoDiaChi(String keyword);
}