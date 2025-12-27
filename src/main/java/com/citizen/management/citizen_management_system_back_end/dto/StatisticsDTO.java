package com.citizen.management.citizen_management_system_back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDTO {
    private Long tongHoKhau;
    private Long tongNhanKhau;
    private Long tongPhanAnh;
    private Long phanAnhDangXuLy;
    private Long phanAnhHoanThanh;
    private Long phanAnhQuaHan;
    private Long tongTamTru;
    private Long tongTamVang;
}
