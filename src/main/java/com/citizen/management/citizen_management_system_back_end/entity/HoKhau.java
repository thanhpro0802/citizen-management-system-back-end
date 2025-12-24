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
// --- THÊM DÒNG NÀY ĐỂ FIX LỖI "No serializer found for ByteBuddyInterceptor" ---
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    @JoinColumn(name = "ma_nhan_khau_chu_ho")
    // Dòng này chỉ fix cho trường chuHo, nhưng lỗi của bạn đang bị ở chính class HoKhau
    @JsonIgnoreProperties({"hoKhau", "danhSachTamTru", "danhSachTamVang", "taiKhoan", "hibernateLazyInitializer", "handler"})
    private NhanKhau chuHo;

    @OneToMany(mappedBy = "hoKhau", cascade = CascadeType.ALL)
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