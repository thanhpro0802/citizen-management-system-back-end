package com.citizen.management.citizen_management_system_back_end.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tep_dinh_kem")
@Getter
@Setter
public class TepDinhKem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ma_tep")
    private String maTep;

    @Column(name = "url")
    private String url;

    @Column(name = "ten_file_goc")
    private String tenFileGoc;

    //Quan he
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_phan_anh", referencedColumnName = "ma_phan_anh")
    private PhanAnh phanAnh;
}
