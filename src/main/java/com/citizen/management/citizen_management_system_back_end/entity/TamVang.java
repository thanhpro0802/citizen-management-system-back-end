package com.citizen.management.citizen_management_system_back_end.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tam_vang")
public class TamVang {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ma_tam_vang")
    private String maTamVang;

    @Column(name = "ngay_bat_dau")
    private Date ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private Date ngayKetThuc;

    @Column(name = "ly_do")
    private String lyDo;

    @ManyToOne
    @JoinColumn(name = "ma_nhan_khau")
    private NhanKhau nhanKhau;

    // Getters and Setters
    public String getMaTamVang() {
        return maTamVang;
    }

    public void setMaTamVang(String maTamVang) {
        this.maTamVang = maTamVang;
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

    public NhanKhau getNhanKhau() {
        return nhanKhau;
    }

    public void setNhanKhau(NhanKhau nhanKhau) {
        this.nhanKhau = nhanKhau;
    }
}
