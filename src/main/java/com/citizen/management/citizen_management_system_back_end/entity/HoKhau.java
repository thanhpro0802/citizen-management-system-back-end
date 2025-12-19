package com.citizen.management.citizen_management_system_back_end.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // [Thêm import]

@Entity
@Table(name = "ho_khau") // Theo chuẩn đặt tên của db
@Getter
@Setter
public class HoKhau {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ma_ho_khau")
    private String maHoKhau;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "ngay_dang_ky")
    @Temporal(TemporalType.DATE)
    private Date ngayDangKy;

    // Chủ hộ: FK đến NhanKhau (một nhân khẩu làm chủ hộ)
    @OneToOne
    @JoinColumn(name = "ma_nhan_khau_chu_ho") // FK tới nhân khẩu chủ hộ
    // [THÊM DÒNG NÀY]: Khi load chủ hộ, bỏ qua trường hoKhau bên trong chủ hộ đó để tránh lặp
    @JsonIgnoreProperties({"hoKhau", "danhSachTamTru", "danhSachTamVang", "taiKhoan"})
    private NhanKhau chuHo;

    // Một HoKhau có nhiều NhanKhau (Thành viên hộ)
    @OneToMany(mappedBy = "hoKhau", cascade = CascadeType.ALL, orphanRemoval = true)
    // [THÊM DÒNG NÀY]: Tương tự, khi load thành viên, bỏ qua trường hoKhau bên trong thành viên
    @JsonIgnoreProperties("hoKhau")
    private List<NhanKhau> danhSachThanhVien = new ArrayList<>();

    public List<NhanKhau> getDanhSachThanhVien() {
        return new ArrayList<>(danhSachThanhVien);
    }

    public void addThanhVien(NhanKhau nk) {
        if (!danhSachThanhVien.contains(nk)) {
            danhSachThanhVien.add(nk);
            nk.setHoKhau(this);
        }
    }

    public void removeThanhVien(NhanKhau nk) {
        if (danhSachThanhVien.remove(nk)) {
            nk.setHoKhau(null);
        }
    }

    public void setDanhSachThanhVien(List<NhanKhau> newList) {
        for (NhanKhau nk : new ArrayList<>(danhSachThanhVien)) {
            removeThanhVien(nk);
        }
        if (newList != null) {
            for (NhanKhau nk : newList) {
                addThanhVien(nk);
            }
        }
    }
}
