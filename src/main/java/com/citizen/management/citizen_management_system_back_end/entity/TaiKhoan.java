package com.citizen.management.citizen_management_system_back_end.entity;

import com.citizen.management.citizen_management_system_back_end.enums.EnumVaiTro;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "tai_khoan")
@Getter
@Setter
public class TaiKhoan {

    @Id
    // @GeneratedValue // Bỏ qua nếu bạn muốn tự gán mã như 'user123'
    @Column(name = "ma_tai_khoan")
    private String maTaiKhoan;

    @Column(name = "ten_dang_nhap", unique = true, nullable = false)
    private String tenDangNhap;

    @Column(name = "mat_khau") // Nên là private và không có Getter
    private String matKhau;

    @Enumerated(EnumType.STRING)
    @Column(name = "vai_tro")
    private EnumVaiTro vaiTro;

    // --- Mối quan hệ (Relationships) ---

    // Quan hệ 1:1 với NhanKhau (Một tài khoản thuộc về 1 nhân khẩu)
    // 'mappedBy = "taiKhoan"' nghĩa là: "Hãy tìm thuộc tính 'taiKhoan'
    // bên trong class NhanKhau để biết cách join bảng"
    //@OneToOne(mappedBy = "taiKhoan", fetch = FetchType.LAZY)
    //private NhanKhau nhanKhau;

    // Quan hệ 1:N với PhanAnh (Một tài khoản gửi nhiều phản ánh)
    // 'mappedBy = "nguoiGui"' trỏ đến thuộc tính 'nguoiGui' trong class PhanAnh
    @JsonIgnore
    @OneToMany(mappedBy = "nguoiGui", fetch = FetchType.LAZY)
    private List<PhanAnh> phanAnhDaGui;

    // Quan hệ 1:N với PhanAnh (Một tài khoản (cán bộ) xử lý nhiều phản ánh)
    // 'mappedBy = "canBoPhuTrach"' trỏ đến thuộc tính 'canBoPhuTrach' trong class PhanAnh
    @JsonIgnore
    @OneToMany(mappedBy = "canBoPhuTrach", fetch = FetchType.LAZY)
    private List<PhanAnh> phanAnhDaXuLy;

    // Quan hệ 1:N với LichSuPhanAnh (Một tài khoản thực hiện nhiều lịch sử)
    // 'mappedBy = "taiKhoanThucHien"' trỏ đến thuộc tính 'taiKhoanThucHien' trong class LichSuPhanAnh
    @JsonIgnore
    @OneToMany(mappedBy = "taiKhoanThucHien", fetch = FetchType.LAZY)
    private List<LichSuPhanAnh> lichSuDaThucHien;

    // Constructors (Nếu không dùng Lombok @NoArgsConstructor)
    public TaiKhoan() {
    }
}