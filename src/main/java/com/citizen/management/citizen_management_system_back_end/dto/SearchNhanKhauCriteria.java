package com.citizen.management.citizen_management_system_back_end.dto;

import lombok.Data;

@Data
public class SearchNhanKhauCriteria {
    private String q; // tên hoặc cccd
    private String gioiTinh;
    private String status;
    private Integer ageFrom;
    private Integer ageTo;
    private String maHoKhau;
}
