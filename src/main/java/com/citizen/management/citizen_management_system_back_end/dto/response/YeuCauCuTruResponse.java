package com.citizen.management.citizen_management_system_back_end.dto.response;

import com.citizen.management.citizen_management_system_back_end.enums.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * DTO phản hồi cho yêu cầu cư trú
 */
@Getter
@Setter
public class YeuCauCuTruResponse {

    private String maYeuCau;
    private EnumLoaiYeuCauCuTru loaiYeuCau;
    private String loaiYeuCauText; // Text mô tả loại yêu cầu
    
    // Thông tin người tạo
    private String nguoiTaoMa;
    private String nguoiTaoHoTen;
    private String nguoiTaoCccd;
    private String nguoiTaoSoDienThoai;

    private EnumDoiTuongDeNghi doiTuongDeNghi;

    // Thông tin khai hộ
    private String nguoiDeNghiHoTen;
    private Date nguoiDeNghiNgaySinh;
    private String nguoiDeNghiGioiTinh;
    private String nguoiDeNghiCccd;

    // Thông tin đăng ký
    private EnumLoaiHinhDangKy loaiHinhDangKy;
    private String chuHoHoTen;
    private String chuHoCccd;
    private String diaChiCuTru;

    // Thông tin tạm vắng
    private EnumLoaiTamVang loaiTamVang;
    private String noiDen;
    private Date thoiGianBatDau;
    private Date thoiGianKetThuc;
    private String lyDo;

    // Thông tin điều chỉnh
    private String phanCanDieuChinh;

    // Thông tin xử lý
    private EnumTrangThaiYeuCau trangThai;
    private String trangThaiText; // Text mô tả trạng thái
    private Date ngayTao;
    private Date ngayCapNhat;
    
    private String canBoXuLyMa;
    private String canBoXuLyHoTen;
    private String ghiChu;
    private String lyDoTuChoi;

    private String fileDinhKem;
}
