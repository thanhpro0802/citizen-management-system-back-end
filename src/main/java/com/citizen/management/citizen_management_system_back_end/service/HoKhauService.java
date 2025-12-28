package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.dto.request.DoiChuHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.HoKhauRequest; // <-- Thêm import này
import com.citizen.management.citizen_management_system_back_end.dto.request.NhapHoRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.TachHoRequest;
import com.citizen.management.citizen_management_system_back_end.entity.HoKhau;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import java.util.List;

public interface HoKhauService {
    HoKhau taoMoi(HoKhauRequest request); // Bạn có thể muốn đổi cái này sang DTO sau này (ví dụ: create)

    HoKhau update(String maHoKhau, HoKhauRequest request);

    void xoa(String maHoKhau);
    List<HoKhau> layTatCa();
    HoKhau layTheoId(String maHoKhau);

    // Các chức năng nghiệp vụ
    HoKhau tachHo(String maHoCu, TachHoRequest request);
    HoKhau nhapHo(String maHoNhapVao, NhapHoRequest request);
    HoKhau doiChuHo(String maHoKhau, DoiChuHoRequest request);

    // Phân quyền & Tìm kiếm
    HoKhau layHoKhauCuaToi(TaiKhoan taiKhoan);
    List<HoKhau> timKiemTheoDiaChi(String keyword);
    List<HoKhau> timKiemTheoChuHo(String keyword);
    List<HoKhau> timKiemTongHop(String keyword);
}