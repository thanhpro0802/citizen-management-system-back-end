package com.citizen.management.citizen_management_system_back_end.dto;

import java.util.Date;

public class TamVangDto {
    private String maTamVang;
    private String maNhanKhau;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String lyDo;

    // Getters and Setters
    public String getMaTamVang() {
        return maTamVang;
    }

    public void setMaTamVang(String maTamVang) {
        this.maTamVang = maTamVang;
    }

    public String getMaNhanKhau() {
        return maNhanKhau;
    }

    public void setMaNhanKhau(String maNhanKhau) {
        this.maNhanKhau = maNhanKhau;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }
}
