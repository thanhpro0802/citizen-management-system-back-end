package com.citizen.management.citizen_management_system_back_end.dto;

import lombok.Data;
import java.util.Date;

@Data
public class NhanKhauDto {
    private String maNhanKhau;
    private String hoTen;
    private Date ngaySinh;
    private String gioiTinh;
    private String soCCCD;
    private String queQuan;
    private String danToc;
    private String quanHeVoiChuHo;
    private String maHoKhau; // id ho khau nếu có
    private String status; // THUONG_TRU, TAM_TRU, TAM_VANG, KHAI_TU
}
