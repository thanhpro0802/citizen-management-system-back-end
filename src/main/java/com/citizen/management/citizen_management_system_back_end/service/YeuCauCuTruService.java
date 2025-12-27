package com.citizen.management.citizen_management_system_back_end.service;

import com.citizen.management.citizen_management_system_back_end.dto.request.YeuCauCuTruRequest;
import com.citizen.management.citizen_management_system_back_end.dto.request.XuLyYeuCauCuTruRequest;
import com.citizen.management.citizen_management_system_back_end.dto.response.YeuCauCuTruResponse;
import com.citizen.management.citizen_management_system_back_end.entity.TaiKhoan;
import com.citizen.management.citizen_management_system_back_end.enums.EnumLoaiYeuCauCuTru;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThaiYeuCau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface cho quản lý yêu cầu cư trú
 */
public interface YeuCauCuTruService {

    /**
     * Tạo yêu cầu cư trú mới (dành cho công dân)
     */
    YeuCauCuTruResponse taoYeuCau(YeuCauCuTruRequest request, TaiKhoan nguoiTao);

    /**
     * Lấy danh sách yêu cầu của người dùng hiện tại
     */
    List<YeuCauCuTruResponse> layYeuCauCuaToi(TaiKhoan taiKhoan);

    /**
     * Lấy chi tiết yêu cầu theo mã
     */
    YeuCauCuTruResponse layChiTietYeuCau(String maYeuCau, TaiKhoan taiKhoan);

    /**
     * Hủy yêu cầu (dành cho công dân - chỉ khi đang chờ xử lý)
     */
    YeuCauCuTruResponse huyYeuCau(String maYeuCau, TaiKhoan taiKhoan);

    /**
     * Lấy tất cả yêu cầu (dành cho cán bộ)
     */
    Page<YeuCauCuTruResponse> layTatCaYeuCau(Pageable pageable);

    /**
     * Tìm kiếm yêu cầu theo tiêu chí
     */
    Page<YeuCauCuTruResponse> timKiemYeuCau(
        EnumTrangThaiYeuCau trangThai,
        EnumLoaiYeuCauCuTru loaiYeuCau,
        Pageable pageable
    );

    /**
     * Xử lý yêu cầu (dành cho cán bộ)
     */
    YeuCauCuTruResponse xuLyYeuCau(String maYeuCau, XuLyYeuCauCuTruRequest request, TaiKhoan canBo);

    /**
     * Phê duyệt yêu cầu
     */
    YeuCauCuTruResponse pheDuyetYeuCau(String maYeuCau, String ghiChu, TaiKhoan canBo);

    /**
     * Nhận xử lý yêu cầu (chuyển từ CHO_XU_LY -> DANG_XU_LY)
     */
    YeuCauCuTruResponse nhanXuLyYeuCau(String maYeuCau, TaiKhoan canBo);

    /**
     * Từ chối yêu cầu
     */
    YeuCauCuTruResponse tuChoiYeuCau(String maYeuCau, String lyDoTuChoi, TaiKhoan canBo);

    /**
     * Đếm số yêu cầu theo trạng thái
     */
    long demYeuCauTheoTrangThai(EnumTrangThaiYeuCau trangThai);
}
