package com.citizen.management.citizen_management_system_back_end.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name= "thong_bao")
@Getter @Setter
public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ma_thong_bao")
    private String maThongBao;

    @Column(name = "noi_dung")
    private String noiDung;

    @Column(name = "da_xem")
    private boolean daXem = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "thoi_gian")
    private Date thoiGian;

    @Column(name = "ma_phan_anh_lien_quan")
    private String maPhanAnhLienQuan;
    //Thong bao gui cho ai?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nguoi_nhan", referencedColumnName = "ma_tai_khoan")
    private TaiKhoan nguoiNhan;

}
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name= "thong_bao")
@Getter @Setter
public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ma_thong_bao")
    private String maThongBao;

    @Column(name = "noi_dung")
    private String noiDung;

    @Column(name = "da_xem")
    private boolean daXem = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "thoi_gian")
    private Date thoiGian;

    @Column(name = "ma_phan_anh_lien_quan")
    private String maPhanAnhLienQuan;
    //Thong bao gui cho ai?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nguoi_nhan", referencedColumnName = "ma_tai_khoan")
    @JsonIgnoreProperties(value = {"phanAnhDaGui", "phanAnhDaXuLy", "lichSuDaThucHien", "nhanKhau", "matKhau", "hibernateLazyInitializer", "handler"})
    private TaiKhoan nguoiNhan;

}
