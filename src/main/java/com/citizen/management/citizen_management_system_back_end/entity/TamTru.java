package com.citizen.management.citizen_management_system_back_end.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "tam_tru")
@Getter
@Setter
public class TamTru {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ma_tam_tru")
    private String maTamTru;

    @Column(name = "ngay_bat_dau")
    private Date ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private Date ngayKetThuc;

    @Column(name = "ly_do")
    private String lyDo;

    @ManyToOne
    @JoinColumn(name = "ma_nhan_khau")
    private NhanKhau nhanKhau;
}
