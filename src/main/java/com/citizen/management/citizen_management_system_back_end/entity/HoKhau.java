package com.citizen.management.citizen_management_system_back_end.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class HoKhau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String soHoKhau;
    private String diaChi;
    private String tenChuHo;
    private String soDienThoaiChuHo;

    // Quan hệ: Một hộ khẩu có nhiều nhân khẩu
    @OneToMany(mappedBy = "hoKhau", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NhanKhau> danhSachThanhVien = new ArrayList<>(); // Nên khởi tạo luôn

    // ========== Helper methods (Best practice) ==========
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

    // Nếu muốn set lại toàn bộ list, nên remove từng thành viên cũ, rồi add mới từng thành viên:
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

    // ========== Getter & Setter ==========
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSoHoKhau() { return soHoKhau; }
    public void setSoHoKhau(String soHoKhau) { this.soHoKhau = soHoKhau; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getTenChuHo() { return tenChuHo; }
    public void setTenChuHo(String tenChuHo) { this.tenChuHo = tenChuHo; }

    public String getSoDienThoaiChuHo() { return soDienThoaiChuHo; }
    public void setSoDienThoaiChuHo(String soDienThoaiChuHo) { this.soDienThoaiChuHo = soDienThoaiChuHo; }

    public List<NhanKhau> getDanhSachThanhVien() {
        return new ArrayList<>(danhSachThanhVien);
    }
}
