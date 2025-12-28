package com.citizen.management.citizen_management_system_back_end.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ho_khau")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HoKhau {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ma_ho_khau")
    private String maHoKhau;

    @Column(name = "dia_chi")
    private String diaChi;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_dang_ky")
    private Date ngayDangKy;

    @OneToOne
    @JoinColumn(name = "ma_nhan_khau_chu_ho")
    @JsonIgnoreProperties({
            "hoKhau", "danhSachTamTru", "danhSachTamVang",
            "taiKhoan", "hibernateLazyInitializer", "handler"
    })
    private NhanKhau chuHo;

    // ✅ KHÔNG CASCADE
    @OneToMany(mappedBy = "hoKhau", fetch = FetchType.LAZY)
    private List<NhanKhau> danhSachThanhVien = new ArrayList<>();

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
}
