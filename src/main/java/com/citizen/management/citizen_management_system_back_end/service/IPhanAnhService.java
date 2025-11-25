package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.dto.*;
import com.citizen.management.citizen_management_system_back_end.entity.LichSuPhanAnh;
import com.citizen.management.citizen_management_system_back_end.entity.PhanAnh;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;

import java.util.List;

public interface IPhanAnhService {
    PhanAnh guiPhanAnh(GuiPhanAnhRequest request, TaiKhoan nguoiGui);

    PhanAnh phanCongXuLy(String maPhanAnh, PhanCongRequest request, TaiKhoan nguoiPhanCong);

    void capNhatXuLyNoiBo(String maPhanAnh, XuLyNoiBoRequest request, TaiKhoan canBoXuLy);

    PhanAnh phanHoiCongDan(String maPhanAnh, PhanHoiRequest request, TaiKhoan canBoPhanHoi);

    PhanAnh danhGiaPhanHoi(String maPhanAnh, DanhGiaRequest request, TaiKhoan nguoiDanhGia);

    List<PhanAnh> layDanhSachPhanAnhCuaToi(TaiKhoan nguoiGui);

    PhanAnh layChiTietPhanAnh(String id);

    List<LichSuPhanAnh> layLichSuPhanAnh(String maPhanAnh);

    List<PhanAnh> layTatCaPhanAnh();
}
