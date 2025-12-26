package com.citizen.management.citizen_management_system_back_end.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class NhanKhauDto {
    private Long id;
    private Long idHoKhau;
    private String hoTen;
    private Date ngaySinh;
    private String gioiTinh;
    private String soCCCD;
    private String queQuan;
    private String danToc;
    private String quanHeVoiChuHo;
    private String maHoKhau; // id ho khau nếu có
    private EnumTrangThaiNhanKhau trangThai; // Enum thay vì String

}
