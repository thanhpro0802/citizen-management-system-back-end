package com.citizen.management.citizen_management_system_back_end.entity;

import com.citizen.management.citizen_management_system_back_end.enums.EnumHanhDong;
import com.citizen.management.citizen_management_system_back_end.enums.EnumTrangThai;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "lich_su_phan_anh")
@Getter
@Setter
public class LichSuPhanAnh {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ma_lich_su")
    private String maLichSu;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "thoi_gian")
    private Date thoiGian;

    @Column(name = "hanh_dong")
    private EnumHanhDong hanhDong;

    @Column(name = "noi_dung")
    private String noiDung;

    @Column(name = "trang_thai_moi")
    private EnumTrangThai trangThaiMoi;

    //Quan he
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_phan_anh", referencedColumnName = "ma_phan_anh")
    private PhanAnh phanAnh;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_tai_khoan_thuc_hien", referencedColumnName = "ma_tai_khoan")
    private TaiKhoan taiKhoanThucHien;
}
