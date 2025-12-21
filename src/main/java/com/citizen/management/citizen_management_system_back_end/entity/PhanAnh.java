package com.citizen.management.citizen_management_system_back_end.entity;

import com.citizen.management.citizen_management_system_back_end.enums.EnumMucDoKhanCap;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThai;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "phan_anh")
@Getter
@Setter
public class PhanAnh {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ma_phan_anh")
    private String maPhanAnh;

    @Column(name = "tieu_de")
    private String tieuDe;

    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_hien_tai")
    private EnumTrangThai trangThaiHienTai;

    @Column(name = "linh_vuc")
    private String linhVuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "muc_do_khan_cap")
    private EnumMucDoKhanCap mucDoKhanCap;

    @Temporal(TemporalType.DATE)
    @Column(name = "thoi_han_xu_ly")
    private Date thoiHanXuLy;

    @Column(name = "danh_gia_hai_long")
    private Integer danhGiaHaiLong;

    @Column(name = "gop_y")
    private String gopY;

    @Column(name = "hinh_anh")
    private String hinhAnh;

    @Column(name = "thoi_gian_tao")
    private Date thoiGianTao;

    @Temporal(TemporalType.TIMESTAMP) // Lưu cả ngày và giờ
    @Column(name = "thoi_gian_hoan_thanh")
    private Date thoiGianHoanThanh;

    // --- QUAN TRỌNG: CHẶN VÒNG LẶP Ở ĐÂY ---
    // Chúng ta ignore luôn "nhanKhau" để tránh việc load lan man sang bảng Hộ khẩu
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_tai_khoan_gui", referencedColumnName = "ma_tai_khoan")
    @JsonIgnoreProperties(value = {"phanAnhDaGui", "phanAnhDaXuLy", "lichSuDaThucHien", "nhanKhau", "matKhau", "hibernateLazyInitializer", "handler"})
    private TaiKhoan nguoiGui;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_can_bo_phu_trach", referencedColumnName = "ma_tai_khoan")
    @JsonIgnoreProperties(value = {"phanAnhDaGui", "phanAnhDaXuLy", "lichSuDaThucHien", "matKhau", "hibernateLazyInitializer", "handler"})
    private TaiKhoan canBoPhuTrach;

    @JsonIgnore
    @OneToMany(mappedBy = "phanAnh")
    private List<LichSuPhanAnh> lichSuList;

    @JsonIgnore
    @OneToMany(mappedBy = "phanAnh")
    private List<TepDinhKem> tepDinhKemList;
}
