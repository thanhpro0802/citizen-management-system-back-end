package com.citizen.management.citizen_management_system_back_end.dto.request;

import com.citizen.management.citizen_management_system_back_end.enums.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * DTO cho tạo mới yêu cầu cư trú
 */
@Getter
@Setter
public class YeuCauCuTruRequest {

    @NotNull(message = "Loại yêu cầu không được để trống")
    private EnumLoaiYeuCauCuTru loaiYeuCau;

    @NotNull(message = "Đối tượng đề nghị không được để trống")
    private EnumDoiTuongDeNghi doiTuongDeNghi;

    // === Thông tin khai hộ (nếu doiTuongDeNghi = KHAI_HO) ===
    private String nguoiDeNghiHoTen;
    private Date nguoiDeNghiNgaySinh;
    private String nguoiDeNghiGioiTinh;
    private String nguoiDeNghiCccd;

    // === Thông tin cho ĐĂNG KÝ TẠM TRÚ / THƯỜNG TRÚ ===
    private EnumLoaiHinhDangKy loaiHinhDangKy;
    private String chuHoHoTen;
    private String chuHoCccd;
    private String diaChiCuTru;

    // === Thông tin cho KHAI BÁO TẠM VẮNG ===
    private EnumLoaiTamVang loaiTamVang;
    private String noiDen;
    private Date thoiGianBatDau;
    private Date thoiGianKetThuc;
    private String lyDo;

    // === Thông tin cho ĐIỀU CHỈNH THÔNG TIN ===
    private String phanCanDieuChinh;

    // File đính kèm
    private String fileDinhKem;
}
