package com.citizen.management.citizen_management_system_back_end.entity;

import com.citizen.management.citizen_management_system_back_end.enums.EnumMucDoKhanCap;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThai;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @Column(name = "trang_thai_hien_tai")
    @Enumerated(EnumType.STRING)
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

    //Quan he
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_tai_khoan_gui", referencedColumnName = "ma_tai_khoan")
    private TaiKhoan nguoiGui;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_can_bo_phu_trach", referencedColumnName = "ma_tai_khoan")
    private TaiKhoan canBoPhuTrach;

    @JsonIgnore
    @OneToMany(mappedBy = "phanAnh")
    private List<LichSuPhanAnh> lichSuList;

    @JsonIgnore
    @OneToMany(mappedBy = "phanAnh")
    private List<TepDinhKem> tepDinhKemList;
}
