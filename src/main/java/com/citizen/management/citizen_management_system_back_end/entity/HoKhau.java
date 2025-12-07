package com.citizen.management.citizen_management_system_back_end.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ho_khau")
@Getter
@Setter
public class HoKhau {

    @Id
    @Column(name = "ma_ho_khau")
    private String maHoKhau;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "ngay_lap")
    private Date ngayLap;

    @OneToMany(mappedBy = "hoKhau", fetch = FetchType.LAZY)
    private List<NhanKhau> danhSachNhanKhau;
}
