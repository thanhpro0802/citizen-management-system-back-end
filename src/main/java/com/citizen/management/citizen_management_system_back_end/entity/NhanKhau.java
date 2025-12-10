package com.citizen.management.citizen_management_system_back_end.entity;

import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThaiNhanKhau;
import jakarta.persistence.*;
import java.util.List;
import java.util.Date;

@Entity
@Table(name = "nhan_khau")
public class NhanKhau {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ma_nhan_khau")
    private String maNhanKhau;

    @Column(name = "ho_ten")
    private String hoTen;

    @Column(name = "ngay_sinh")
    private Date ngaySinh;

    @Column(name = "gioi_tinh")
    private String gioiTinh;

    @Column(name = "so_cccd", unique = true)
    private String soCCCD;

    @Column(name = "que_quan")
    private String queQuan;

    @Column(name = "dan_toc")
    private String danToc;

    @Column(name = "quan_he_voi_chu_ho")
    private String quanHeVoiChuHo;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private EnumTrangThaiNhanKhau trangThai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_ho_khau")
    private HoKhau hoKhau;

    @OneToMany(mappedBy = "nhanKhau", fetch = FetchType.LAZY)
    private List<TamTru> danhSachTamTru;

    @OneToMany(mappedBy = "nhanKhau", fetch = FetchType.LAZY)
    private List<TamVang> danhSachTamVang;

    // Getters and Setters
    public String getMaNhanKhau() {
        return maNhanKhau;
    }

    public void setMaNhanKhau(String maNhanKhau) {
        this.maNhanKhau = maNhanKhau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSoCCCD() {
        return soCCCD;
    }

    public void setSoCCCD(String soCCCD) {
        this.soCCCD = soCCCD;
    }

    public String getQueQuan() {
        return queQuan;
    }

    public void setQueQuan(String queQuan) {
        this.queQuan = queQuan;
    }

    public String getDanToc() {
        return danToc;
    }

    public void setDanToc(String danToc) {
        this.danToc = danToc;
    }

    public String getQuanHeVoiChuHo() {
        return quanHeVoiChuHo;
    }

    public void setQuanHeVoiChuHo(String quanHeVoiChuHo) {
        this.quanHeVoiChuHo = quanHeVoiChuHo;
    }

    public EnumTrangThaiNhanKhau getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(EnumTrangThaiNhanKhau trangThai) {
        this.trangThai = trangThai;
    }

    public HoKhau getHoKhau() {
        return hoKhau;
    }

    public void setHoKhau(HoKhau hoKhau) {
        this.hoKhau = hoKhau;
    }

    public List<TamTru> getDanhSachTamTru() {
        return danhSachTamTru;
    }

    public void setDanhSachTamTru(List<TamTru> danhSachTamTru) {
        this.danhSachTamTru = danhSachTamTru;
    }

    public List<TamVang> getDanhSachTamVang() {
        return danhSachTamVang;
    }

    public void setDanhSachTamVang(List<TamVang> danhSachTamVang) {
        this.danhSachTamVang = danhSachTamVang;
    }
}
